package filters;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ldb.RDFTriple;
import result.Result;
import result.ResultComposite;
import result.Table;
import utils.Utils;
import filter.Filter;
import filter.FilterTable;

/**
 * Skeleton for a sorting filter.
 * 
 * Just implement comparator() to return a comparator on RDFTriple that suits your needs.
 * 
 */
public abstract class SortFilter extends Filter {

	@Override
	public Result execute(Table table) {

		List<RDFTriple> solutions = Utils.cloneTriples(table);
		Collections.sort(solutions, comparator());
		return new Table(solutions);
	}
	
	@Override
	public Result execute(ResultComposite composite) {
		ResultComposite result = new ResultComposite();
		for(Result r : composite.getChildren())
			if(r instanceof Table) {
				result.add(execute((Table)r));
			}
			else if(r instanceof ResultComposite) {
				result.add(execute((ResultComposite)r));
			}
		return result;
	};

	/**
	 * Implement this to return a comparator that suits your needs.
	 * 
	 * @return comparator on RDFTriple
	 */

	protected abstract Comparator<RDFTriple> comparator();

}
