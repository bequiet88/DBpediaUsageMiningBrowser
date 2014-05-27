package filters;

import java.util.Comparator;

import ldb.RDFTriple;

/**
 * Sorts results alphabetically by subject, then predicate, then object
 * 
 */
public class SortLexicographic extends SortFilter {

	private boolean ascending = true;

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	@Override
	protected Comparator<RDFTriple> comparator() {
		

		
		return new Comparator<RDFTriple>() {
			
			@Override
			// implementation of Comparator on RDFTriple
			public int compare(RDFTriple o1, RDFTriple o2) {
				int res;
				// sort first by subject
				res = o1.getSub().compareTo(o2.getSub());
				if (res == 0) // both subjects identical
					// then by predicate
					res = o1.getPred().compareTo(o2.getPred());
				if (res == 0) // both predicates identical
					// then by object
					res = o1.getObj().compareTo(o2.getObj());
				// if not ascending reverse order
				return ascending ? res : res * -1;
			}
			
		};
	}
}
