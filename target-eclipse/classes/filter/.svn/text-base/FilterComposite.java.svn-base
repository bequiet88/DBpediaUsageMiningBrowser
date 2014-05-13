package filter;

import result.Result;
import result.ResultComposite;
import result.Table;

/**
 * interface for filters, which work an a ResultComposite and which return either a Table or a ResultComposite.
 * 
 */

public abstract class FilterComposite extends Filter {

	/**
	 * Executes the filter on the given composite.
	 * 
	 * @param composite
	 *            composite to execute filter on
	 * @return the filtered results
	 */
	public abstract Result execute(ResultComposite composite);

	/**
	 * do not use; a FilterComposite should filter ResultComposites.
	 */
	public Result execute(Table table) {
		return null;
	}

}
