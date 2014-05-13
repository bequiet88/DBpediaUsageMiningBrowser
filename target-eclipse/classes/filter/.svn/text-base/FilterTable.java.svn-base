package filter;

import result.Result;
import result.ResultComposite;
import result.Table;

/**
 * interface for filters, which work an a Table and which return either a Table or a ResultComposite.
 * 
 */

public abstract class FilterTable extends Filter {

	/**
	 * Executes the filter on the given table.
	 * 
	 * @param table
	 *            table to execute the filter on
	 * @return the filtered results
	 */
	public abstract Result execute(Table table);

	/**
	 * do not use; a FilterTable should filter Tables
	 */
	public Result execute(ResultComposite composite) {
		return null;
	}

}
