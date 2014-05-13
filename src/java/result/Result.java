package result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;

import filter.Filter;

/**
 * Interface for the components of our composite pattern
 * 
 */

public abstract class Result extends Taggable implements Metadata {

	private static final long serialVersionUID = 1L;

	protected String caption = null;

	protected Map<String, String> metadata = new HashMap<String, String>();
	
	protected Model model;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		if (caption == null) {
			if (other.caption != null)
				return false;
		} else if (!caption.equals(other.caption))
			return false;
		return true;
	}

	public String getCaption() {
		return caption;
	}
	
	public Model getUnderlyingModel() {
		return model;
	}
	
	public void setUnderlyingModel(Model m) {
		model = m;
		for(Result r : getChildren()) {
			r.setUnderlyingModel(m);
		}
	}

	public abstract List<Result> getChildren();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		return result;
	}

	/**
	 * Sets the caption for this result.
	 * 
	 * @param caption
	 *            the caption to set
	 */

	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * Gets the number of direct sub-results.
	 * 
	 * @return number of direct sub-results
	 */
	public abstract int size();

	/**
	 * Applies the filter to all sub-results.
	 * 
	 * @param filter
	 *            the filter to be applied
	 * @param parameters
	 *            parameters for the filter
	 * @return filtered results
	 */
	public abstract Result use(Filter filter);

	@Override
	public void addMetaData(String name, String value) {
		metadata.put(name, value);
	}

	@Override
	public String getMetadata(String name) {
		return metadata.get(name);

	}

	@Override
	public Map<String, String> getMetadata() {
		return metadata;
	}

}
