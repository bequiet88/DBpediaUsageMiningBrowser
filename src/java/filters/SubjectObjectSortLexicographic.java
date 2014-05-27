package filters;

import java.util.Comparator;

import ldb.RDFTriple;

/**
 * Sorts results alphabetically by subject, then predicate, then object
 * 
 */
public class SubjectObjectSortLexicographic extends SortFilter {

	private boolean ascending = true;

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	@Override
	protected Comparator<RDFTriple> comparator() {
		
		final String RES = this.processedResource;
		
		return new Comparator<RDFTriple>() {
			
			@Override
			// implementation of Comparator on RDFTriple
			public int compare(RDFTriple o1, RDFTriple o2) {

				int res;
				
				if(o1.getSub().equals(RES) && o2.getSub().equals(RES)) {
					res = lexicCompare(o1, o2);
				}
				else if(!o1.getSub().equals(RES) && !o2.getSub().equals(RES)) {
					res = lexicCompare(o1, o2);
				}
				else if(o1.getSub().equals(RES) && !o2.getSub().equals(RES)) {
					res = -10;
				}
				else {
					res = 10;
				}
				
				// if not ascending reverse order
				return ascending ? res : res * -1;
			}
			
			private int lexicCompare(RDFTriple o1, RDFTriple o2) {
				// sort first by subject
				int res = o1.getSub().compareTo(o2.getSub());
				if (res == 0) // both subjects identical
					// then by predicate
					res = o1.getPred().compareTo(o2.getPred());
				if (res == 0) // both predicates identical
					// then by object
					res = o1.getObj().compareTo(o2.getObj());
				return res;
			}
			
		};
	}
}
