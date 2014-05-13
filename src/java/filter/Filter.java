package filter;

import com.hp.hpl.jena.rdf.model.Model;

import result.Result;
import result.ResultComposite;
import result.Table;

/**
 * Super interface for all filters.
 * 
 */
public abstract class Filter {

	/**
	 * Contains the name/URI of the resource this filter is processing.
	 */
	protected String processedResource = null;
	
	/**
	 * Contains the model underlying the resource
	 */
	protected Model model = null;

	/**
	 * Sets the resource processed by this filter.
	 * 
	 * @param processedResource
	 *            the resource in process
	 */
	public void setProcessedResource(String processedResource) {
		this.processedResource = processedResource;
	}
	
	public void setModel(Model m) {
		model = m;
	}

	/**
	 * Executes the filter on the given table.
	 * 
	 * @param table
	 *            table to execute the filter on
	 * @return the filtered results or null if FilterComposite
	 */

	public abstract Result execute(Table table);

	/**
	 * Executes the filter on the given composite.
	 * 
	 * @param composite
	 *            composite to execute filter on
	 * @return the filtered results or null if FilterTable
	 */

	public abstract Result execute(ResultComposite composite);

}
