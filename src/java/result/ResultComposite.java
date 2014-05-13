package result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import filter.Filter;
import filter.FilterTable;

/**
 * Node of the results tree.
 * 
 * Holds a number of Tables and/or other ResultComposites and provides access to them.
 * 
 */

public class ResultComposite extends Result {

	private static final long serialVersionUID = 1L;

	private List<Result> components;

	public ResultComposite() {
		this.components = new ArrayList<Result>();
	}

	public ResultComposite(List<Result> components) {
		super();
		if (components == null)
			this.components = null;
		else
			this.components = new ArrayList<Result>(components);
	}

	public ResultComposite(Result[] components) {
		super();
		this.components = new ArrayList<Result>(Arrays.asList(components));
	}

	/**
	 * Adds the result to components.
	 * 
	 * @param result
	 */
	public void add(Result result) {
		result.setUnderlyingModel(model);
		components.add(result);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultComposite other = (ResultComposite) obj;
		if (components == null) {
			if (other.components != null)
				return false;
		} else if (!components.equals(other.components))
			return false;
		return true;
	}

	/**
	 * Returns the component at the specified index.
	 * 
	 * @param index
	 *            Index of the component to return
	 * @return Component at index
	 */

	public Result get(int index) {
		return components.get(index);
	}

	@Override
	public List<Result> getChildren() {
		return components;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((components == null) ? 0 : components.hashCode());
		return result;
	}

	/**
	 * Returns the number of components i.e. children (not the number of all nodes beneath this one!).
	 */

	public int size() {
		return components.size();
	}

	/**
	 * Applies the filter to this node and all sub-nodes.
	 */

	@Override
	public Result use(Filter filter) {
		Result result;
		if (filter instanceof FilterTable) {
			List<Result> newComponent = new ArrayList<Result>();
			for (Result r : components) {
				r = r.use(filter);
				r.setUnderlyingModel(model);
				newComponent.add(r);
			}
			result = new ResultComposite(newComponent); // new ResultComposite(newComponent);
			result.setUnderlyingModel(model);
			result.addTags(tags); // tags and caption shall not be lost
			result.setCaption(caption);
		} else {
			result = filter.execute(this);
			result.setUnderlyingModel(model);
		}
		return result;
	}

}
