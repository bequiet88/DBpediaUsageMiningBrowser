package filters;

import java.util.ArrayList;
import java.util.List;

import ldb.RDFTriple;
import result.Result;
import result.ResultComposite;
import result.Table;
import filter.Filter;

/**
 * Abstract class for filtering by single entries.
 * 
 * Just implement fits to return true iff you want that triple in your result.
 * 
 */
public abstract class SelectionFilter extends Filter {

	/**
	 * Given one table this return a new table with all entries for which fits() is true.
	 */
	@Override
	public final Result execute(Table table) {
		List<RDFTriple> filtered = new ArrayList<RDFTriple>();
		for (RDFTriple triple : table.getTriples())
			if (fits(triple))
				filtered.add(triple);

		Table res = new Table(filtered);
		res.setCaption(caption());
		res.addTags(tags());
		return res;
	}

	@Override
	public final Result execute(ResultComposite resultComposite) {
		ResultComposite result = new ResultComposite();
		for (int i = 0; i < resultComposite.size(); i++) {
			if (resultComposite.get(i) instanceof Table) {
				List<RDFTriple> triples = new ArrayList<RDFTriple>();
				for (RDFTriple triple : ((Table) resultComposite.get(i)).getTriples()) {
					if (fits(triple))
						triples.add(triple);
				}
				// seems better this way because it doesn't change the tree structure
				// if (triples.size() != 0) {
				Table t = new Table(triples);
				t.addTags(resultComposite.get(i).getTags());
				result.add(t);
				// }

			}

			else {

				result.add(execute((ResultComposite) resultComposite.get(i)));
			}
		}
		return result;
	}

	/**
	 * Override this if you want a caption to be set.
	 * 
	 * @return caption to set
	 */
	protected String caption() {
		return null;
	}

	/**
	 * Override this if you want tags to be set.
	 * 
	 * @return tags to set
	 */
	protected String[] tags() {
		return new String[0];
	}

	/**
	 * Implement this to return true iff you want this triple in your results.
	 * 
	 * @param triple
	 *            triple to check
	 * @return true iff you want it in your results
	 */
	protected abstract boolean fits(RDFTriple triple);

}
