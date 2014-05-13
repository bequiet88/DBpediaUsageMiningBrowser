package filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class AutomaticTypeCompletion {

	/**
	 * Parameters
	 */
	private float minConfidence = 0.05f;
	private float minConfidenceFinal = 0.15f;
	private float minSupport = 0.1f;
	private float subsetSmallerSignificanceLevel = 0.05f;
	private int maxTypeSize = 100000;
	// at least x% of all classes must agree on discarding 
	private float minThresholdForDisjointnessFiltering = 0.1f;
	private int maxSubsetSize = 2;
	
	/**
	 * Caches
	 */
	private Map<String,Integer> simpleCountCache = new HashMap<String,Integer>(); 
	private Map<Set<String>,Integer> multiCountCache = new HashMap<Set<String>,Integer>();

	/**
	 * Working variables
	 */
	private Map<Set<String>,Float> supportValues = new HashMap<Set<String>,Float>();
	private Map<String,Float> inverseConfidenceValues = new HashMap<String,Float>();
	private Set<String> directTypes = new HashSet<String>();
	private List<CountedType> queue = new LinkedList<CountedType>();
	private List<CountedType> processedElements = new LinkedList<CountedType>();
	
	/**
	 * Other stuff
	 */
	private SPARQLEndpoint endpoint;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AutomaticTypeCompletion ATC = new AutomaticTypeCompletion("http://dbpedia.org/sparql");

		ATC.runTests();

//		String instance = "http://dbpedia.org/resource/Technische_Universit%C3%A4t_Darmstadt";
		// this is an amazing example, as it finds http://dbpedia.org/class/yago/EduCAtionalInstitutionsEstablishedIn1971
//		String instance = "http://dbpedia.org/resource/Darmstadt_University_of_Applied_Sciences";
		// here, something goes wrong with filtering due to bad coverage of YAGO mappings 
//		String instance = "http://dbpedia.org/resource/Der_Spiegel";
		// nice example
//		String instance = "http://dbpedia.org/resource/Resource_Description_Framework";
//		String instance = "http://dbpedia.org/resource/Technische_Universit%C3%A4t_Darmstadt";
//		ATC.getAdditionalTypes(instance);
//		ATC.getAdditionalTypesFromTransitiveClosure(instance);
	}
	
	public void runTests() {
		String query = "SELECT DISTINCT ?x FROM <http://dbpedia.org> WHERE {?x a owl:Thing. } ORDER BY RAND() LIMIT 150";
		ResultSet RS = endpoint.runSelect(query);
		int count = 1;
		while(RS.hasNext()) {
			System.out.println("round " + count++);
			String resource = RS.next().getResource("x").toString();

			System.out.println("Transitive Closure: ");
			Date d0 = new Date();
			getAdditionalTypesFromTransitiveClosure(resource);
			Date d1 = new Date();
			System.out.println("-> Time: " + (d1.getTime()-d0.getTime()));			
			
			for(int i=4;i>0;i--) {
				System.out.println("MaxSubsetSize: " + i);
				maxSubsetSize = i;
				d0 = new Date();
				getAdditionalTypes(resource);
				d1 = new Date();
				System.out.println("-> Time: " + (d1.getTime()-d0.getTime()));
			}
		}
	}

	public AutomaticTypeCompletion(String endpointURL) {
		endpoint = new SPARQLEndpoint(endpointURL);
	}
	
	/**
	 * This is called after each run to reset local variables
	 * and prevent memory leaks
	 */
	private void reset() {
		supportValues.clear();
		inverseConfidenceValues.clear();
		directTypes.clear();
		queue.clear();
		processedElements.clear();
		
		// multi count cache is also cleared, as it would become too large
		multiCountCache.clear();
	}
	
	/**
	 * Get a list of types for a given resource
	 * @param resourceURI e.g. http://dbpedia.org/resource/Darmstadt
	 * @return
	 */
	public synchronized Collection<RatedType> getAdditionalTypes(String resourceURI) {
		reset();
		System.out.println("Instance: " + resourceURI + "; algorithm: lazy association rules");
		// 1st step: collect direct types
		List<CountedType> directTypes = getDirectTypes(resourceURI);
		System.out.println("direct types:");
		for(CountedType CT : directTypes) {
			this.directTypes.add(CT.types.iterator().next());
			System.out.println(CT);
		}

		Set<CountedType> resultSet = new HashSet<CountedType>();
		
		for(CountedType CT : directTypes) {
			if(CT.count<maxTypeSize) {
				sortInQueue(CT);
				inverseConfidenceValues.put(CT.types.iterator().next(),0.0f);
				supportValues.put(CT.types,1.0f);
			}
		}
		
		int examinedSets = 0;
		while(queue.size()>0) {
			CountedType CT = queue.remove(0);
			examinedSets++;
			processedElements.add(CT);
			// System.out.println("Examine " + CT);
			// find next possible subsets and arrange in queue
			for(CountedType newType : get1plusSubsetsWithMinSupport(CT)) {
				// determine support of this subset
				float support = ((float) newType.count) / getSmallestType(newType.types);
				supportValues.put(newType.types, support);
				if(support>minSupport) {
					sortInQueue(newType);
				}
			}
			
			// find candidate types from this subset
			Collection<CountedType> candidates = getCandidateTypes(CT);
			for(CountedType CTnew : candidates) {
				if(!directTypes.contains(CTnew))
					resultSet.add(CTnew);
			}
		}
		System.out.println("Total itemsets examined: " + examinedSets);
		
		System.out.println("~~~");
		// discard types disjoint with a given percentage of direct types
//		List<RatedType> results = new LinkedList<RatedType>();
//		Set<String> processedTypes = new HashSet<String>();
//		// sort direct types in ascending order for faster processing
//		Collections.sort(directTypes);
//		for(CountedType CT : resultSet) {
//			String foundType = CT.types.iterator().next();
//			if(!processedTypes.contains(foundType)) {
//				float discardScore = 0.0f;
//				int totalCount = directTypes.size();
//				for(CountedType CTdirect : directTypes) {
//					if(areDisjoint(foundType,CTdirect.types.iterator().next())) {
//						discardScore++;
//						// quick exit to omit unnecessary queries
//						if(discardScore / totalCount>=minThresholdForDisjointnessFiltering)
//							break;
//					}
//				}
//				if(discardScore/totalCount<=minThresholdForDisjointnessFiltering) {
//					results.add(new RatedType(foundType,confidenceValues.get(foundType)));
//				}
//				else
//					System.out.println("discarded " + foundType + " (c=" + confidenceValues.get(foundType) + ") after disjoint check, discard score >= " + discardScore/totalCount);
//				processedTypes.add(foundType);
//			}
//		}
		
		// discard types disjoint with AT LEAST ONE DIRECT TYPE
		// sort direct types in ascending order for faster processing
		List<RatedType> results = new LinkedList<RatedType>();
		Collections.sort(directTypes);
		Set<String> processedTypes = new HashSet<String>();
		for(CountedType CT : resultSet) {
			String foundType = CT.types.iterator().next();
			// discard types by threshold
			if(1-inverseConfidenceValues.get(foundType)<minConfidenceFinal) {
				// System.out.println("discarding " + foundType + " (c=" + (1-inverseConfidenceValues.get(foundType)) + ") below threshold");
			}
			else if(!processedTypes.contains(foundType)) {
				boolean disjoint = false;
				String disjointType = null;
				for(CountedType CTdirect : directTypes) {
					if(areDisjoint(foundType,CTdirect.types.iterator().next())) {
						disjoint = true;
						disjointType = CTdirect.types.iterator().next();
						break;
					}
				}
				if(!disjoint) {
					results.add(new RatedType(foundType,1-inverseConfidenceValues.get(foundType)));
				}
				else {
					// System.out.println("discarded " + foundType + " (c=" + (1-inverseConfidenceValues.get(foundType)) + ") after disjoint check, disjoint with " + disjointType);
				}
				processedTypes.add(foundType);
			}
		}
		System.out.println("~~~");
//		filterByMutualDisjointness(results);
//		Iterator<RatedType> it = results.iterator();
//		while(it.hasNext()) {
//			RatedType RT = it.next();
//			if(RT.getRating()<minConfidence) {
//				System.out.println("discarded " + RT + " after mutual disjoint check");
//				it.remove();
//			}
//		}
//		System.out.println("~~~");
		
		Collections.sort(results);
		Collections.reverse(results);
		
		for(RatedType RT : results)
			System.out.println("found " + RT);
		
		return results;
	}
	
	public List<RatedType> getAdditionalTypesFromTransitiveClosure(String resourceURI) {
		Collection<CountedType> originalTypes = getDirectTypes(resourceURI);
		List<RatedType> result = new LinkedList<RatedType>();
		System.out.println("Instance: " + resourceURI + "; algorithm: transitive closure");
		String query = "SELECT distinct ?t WHERE  { <" + resourceURI + "> a ?t0 . {  SELECT * WHERE { ?t0 <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?t } }  OPTION (TRANSITIVE, t_distinct, t_in (?t0), t_out (?t) ) . }";
		System.out.println(query);
		ResultSet RS = endpoint.runSelect(query);
		while(RS.hasNext()) {
			QuerySolution QS = RS.next();
			String type = QS.getResource("t").getURI();
			boolean isNew = true;
			for(CountedType CT : originalTypes)
				if(CT.types.iterator().next().equals(type))
					isNew = false;
			if(isNew) {
				result.add(new RatedType(type,1.0f));
				System.out.println(type);
			}
		}
		System.out.println("~~~");
		return result;
	}
	
	/**
	 * Gets the candidate types for a set fulfilling min confidence
	 * @param CT
	 * @return
	 */
	private List<CountedType> getCandidateTypes(CountedType CT) {
		List<CountedType> result = new LinkedList<CountedType>();
		// convert minSupport to minimum cardinality of intersection with new type
		int minNumber = (int) (CT.count * minConfidence / (getConfidence(CT.types)));
		String queryForRelatedTypes = "SELECT ?t ?c WHERE { {select ?t COUNT(?x) as ?c where {";
		for(String s : CT.types)
			queryForRelatedTypes += "?x a <"  +s + "> . ";
		queryForRelatedTypes += "?x a ?t} } FILTER (?c>" + minNumber + ") }";
		// System.out.println(queryForRelatedTypes);
		ResultSet RS = endpoint.runSelect(queryForRelatedTypes);
		while(RS.hasNext()) {
			QuerySolution QS = RS.next();
			CountedType CTnew = new CountedType(QS.getResource("t").getURI(), QS.getLiteral("c").getInt());
			float confidence = 1-(float)(CTnew.count)/CT.count;
			// System.out.println("found " + CTnew.types.iterator().next() + " with confidence " + (1-confidence));
			if(!inverseConfidenceValues.containsKey(CTnew.types.iterator().next()))
				inverseConfidenceValues.put(CTnew.types.iterator().next(), confidence);
			else
				// compute a product of inverse probabilities
				inverseConfidenceValues.put(CTnew.types.iterator().next(),inverseConfidenceValues.get(CTnew.types.iterator().next())*confidence);
			result.add(CTnew);
		}
		return result;
	}
	
	/**
	 * Gets all useful subsets for a type which are one element larger,
	 * respect minimum support and maximum subset size, 
	 * have more than one element (i.e., contain more elements 	 
	 * than the original resource), and are smaller than the original set
	 * @param CT
	 * @return
	 */
	private List<CountedType> get1plusSubsetsWithMinSupport(CountedType CT) {
		List<CountedType> result = new LinkedList<CountedType>();
		// if this set has already maximum subset size, there is no need to 
		// expand further
		if(CT.types.size()>=maxSubsetSize)
			return result;
		for(CountedType otherType : queue) {
			// only expand each set by 1
			if(otherType.types.size()==1) {
				String newType = otherType.types.iterator().next();
				if(!CT.types.contains(newType)) {
					Set<String> candidateSet = new HashSet<String>();
					candidateSet.addAll(CT.types);
					candidateSet.add(newType);
					// a set can only be frequent if all its subsets are frequent
					List<Set<String>> allSubsets = getAllSubsets(candidateSet);
					boolean goodSubset = true;
					for(Set<String> subset : allSubsets)
						if(getSupport(subset)<minSupport) {
							goodSubset = false;
							break;
						}
					if(goodSubset) {
						int count = getCountForType(candidateSet);
						// new set must be smaller than original set and larger than one element
						if(count>1 && count<=(1-subsetSmallerSignificanceLevel)*CT.count) {
							float support = getSupport(candidateSet);
							// new set must be smaller than original set
							if(support>minSupport)
								result.add(new CountedType(candidateSet,count));
						}
					}
				}
			}
		}
		return result;
	}
	
	private float getSupport(Set<String> types) {
		if(supportValues.containsKey(types))
			return supportValues.get(types);
		int count = getCountForType(types);
		// determine support of this subset
		float support = ((float) count) / getSmallestType(types);
		supportValues.put(types, support);
		return support;
	}
	
	/**
	 * Confidence of a complex type is computed as the product
	 * of single confidences
	 * @param types
	 * @return
	 */
	private float getConfidence(Set<String> types) {
		float confidence = 1.0f;
		for(String s : types)
			confidence *= (1-inverseConfidenceValues.get(s));
		return confidence;
	}
	
	/**
	 * Get the size of the smallest elementary type in a set of types
	 * @param set
	 * @return
	 */
	private int getSmallestType(Set<String> set) {
		int i = Integer.MAX_VALUE;
		for(String s : set)
			i = Math.min(i,getCountForType(s));
		return i;
	}
	
	/**
	 * Gets a list of subsets of a set, ordered by set cardinality
	 * @param originalSet
	 * @return
	 */
	private List<Set<String>> getAllSubsets(Set<String> originalSet) {
		List<Set<String>> result = new LinkedList<Set<String>>();
		Set<Set<String>> directSmaller = getSubsetsOneSmaller(originalSet);
		result.addAll(directSmaller);
		for(Set<String> smallerSet : directSmaller)
			result.addAll(getAllSubsets(smallerSet));
		return result;
	}
	
	/**
	 * Get a set of all subsets with n-1 elements for a set with n elements
	 * @param originalSet the set with n elements
	 * @return
	 */
	private Set<Set<String>> getSubsetsOneSmaller(Set<String> originalSet) {
		Set<Set<String>> result = new HashSet<Set<String>>();
		if(originalSet.size()==1)
			return result;
		for(String s : originalSet) {
			Set<String> set0 = new HashSet<String>();
			set0.addAll(originalSet);
			set0.remove(s);
			result.add(set0);
		}
		return result;
	}

	/**
	 * Gets the count for a disjunction of types
	 * @param types
	 * @return
	 */
	private int getCountForType(Set<String> types) {
		if(types.size()==1)
			return getCountForType(types.iterator().next());
		if(multiCountCache.containsKey(types))
			return multiCountCache.get(types);
		String query = "SELECT COUNT(?x) AS ?c WHERE { ";
		for(String uri : types)
			query += "?x a <" + uri + ">. ";
		query += "}";
		ResultSet RS = endpoint.runSelect(query);
		int c = RS.next().getLiteral("c").getInt();
		multiCountCache.put(types,c);
		return c;
	}
	
	/**
	 * Gets the count for a single type
	 * Single type counts are cached
	 * @param type
	 * @return
	 */
	private int getCountForType(String type) {
		if(simpleCountCache.containsKey(type))
			return simpleCountCache.get(type);
		String query = "SELECT COUNT(?x) AS ?c WHERE { ?x a <" + type + ">}";
		ResultSet RS = endpoint.runSelect(query);
		int c = RS.next().getLiteral("c").getInt();
		simpleCountCache.put(type,c);
		return c;
	}
	
	/**
	 * Gets the initial list of direct types
	 * @param resource
	 * @return
	 */
	private List<CountedType> getDirectTypes(String resource) {
		List<CountedType> result = new LinkedList<CountedType>();
		
		String queryForOriginalTypes = "select distinct ?t where {<" + resource + "> a ?t}";
		ResultSet RS = endpoint.runSelect(queryForOriginalTypes);
		while(RS.hasNext()) {
			QuerySolution QS = RS.next();
			String type = QS.get("t").toString();
			int size = getCountForType(type);
			CountedType CT = new CountedType(type,size);
			result.add(CT);
		}

//		String queryForOriginalTypes = "select distinct ?t COUNT(?x) AS ?c where {<" + resource + "> a ?t . ?x a ?t}";
//		ResultSet RS = endpoint.runSelect(queryForOriginalTypes);
//		while(RS.hasNext()) {
//			QuerySolution QS = RS.next();
//			String uri = QS.get("t").toString();
//			int size = QS.getLiteral("c").getInt();
//			CountedType CT = new CountedType(uri,size);
//			result.add(CT);
//		}
		return result;
	}
	
	/**
	 * Sort a new element into the queue of elements to be processed
	 * Sorting order: smallest elements first
	 * @param newElement
	 */
	private void sortInQueue(CountedType newElement) {
		if(queue.contains(newElement) || processedElements.contains(newElement))
			return;
		if(queue.isEmpty())
			queue.add(newElement);
		else {
			int position = 0;
			if(queue.get(0).count<newElement.count)
				for(int i=0;i<queue.size();i++) {
					CountedType CT = queue.get(i);
					if(CT.count<=newElement.count)
						position = i+1;
				}
			queue.add(position, newElement);
		}
	}
	
	private boolean areDisjoint(String type1, String type2) {
		String query = "ASK {?x a <" + type1 + "> . ?x a <" + type2 + ">}";
		if(!endpoint.runAsk(query)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Filters results by computing mutual disjointness maluses
	 * @param results
	 * @return
	 */
	private void filterByMutualDisjointness(List<RatedType> results) {
		// sort by rating for faster processing -- bigger malusus first
		List<RatedType> resultsSortedByRating = new ArrayList<RatedType>();
		resultsSortedByRating.addAll(results);
		Collections.sort(resultsSortedByRating);
		Collections.reverse(resultsSortedByRating);
		
		for(RatedType RT1 : results) {
			float minRating = RT1.getRating();
			// quick check: can we discard this type at all?
			for(RatedType RT2 : results)
				if(!RT1.equals(RT2))
					minRating = minRating * (1-RT2.getRating());
			if(minRating<minConfidence)
				for(int i=0;i<resultsSortedByRating.size();i++) {
					RatedType RT2 = resultsSortedByRating.get(i);
					if(RT1!=RT2)
						if(areDisjoint(RT1.getURI(),RT2.getURI())) {
							RT1.setRating(RT1.getRating()*(1-RT2.getRating()));
							// early termination: minConfidence already reached
							if(RT1.getRating()<minConfidence)
								break;
						}
						minRating = RT1.getRating();
						// early termination: we cannot get under minConfidence anymore
						for(int k=i+1;k<resultsSortedByRating.size();k++) {
							RatedType RT3 = resultsSortedByRating.get(k);
							if(!RT1.equals(RT3))
								minRating = minRating * RT3.getRating();
						}
						if(minRating>=minConfidence)
							break;
				}
		}
	}


	
	private class CountedType implements Comparable<CountedType> {
		Set<String> types;
		
		Integer count;
		
		/**
		 * convenience constructor for single types
		 * @param type
		 * @param count
		 */
		public CountedType(String type, int count) {
			types = new HashSet<String>();
			types.add(type);
			this.count = count;
		}
		
		public CountedType(Set<String> types, int count) {
			this.types = types;
			this.count = count;
		
		}
		
		public String toString() {
			return types + " (" + count + ")";
		}
		
		@Override
		public boolean equals(Object arg0) {
			CountedType other = (CountedType) arg0;
			return types.containsAll(other.types) && other.types.containsAll(types);
		}

		@Override
		public int compareTo(CountedType other) {
			return count.compareTo(other.count);
		}
	}

}
