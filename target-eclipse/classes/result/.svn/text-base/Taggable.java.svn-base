package result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * Enables adding tags and checking for them.
 * 
 */

public class Taggable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * list to hold the tags
	 */
	protected List<String> tags = new ArrayList<String>();

	/**
	 * Adds a tag to this result.
	 * 
	 * @param tag
	 *            tag to add
	 */

	public void addTag(String tag) {
		tags.add(tag);

	}

	/**
	 * Returns the list of tags.
	 * 
	 * @return list of tags
	 */

	public List<String> getTags() {
		return tags;
	}

	/**
	 * Adds all tags.
	 * 
	 * @param tags
	 *            tags to add
	 */

	public void addTags(List<String> tags) {
		for (String tag : tags)
			if (!this.tags.contains(tag))
				this.tags.add(tag);
	}

	/**
	 * Adds all tags.
	 * 
	 * @param tags
	 *            tags to add
	 */
	public void addTags(String[] tags) {
		addTags(new ArrayList<String>(Arrays.asList(tags)));
	}

	/**
	 * Checks if this result has a certain tag.
	 * 
	 * @param tag
	 *            tag to check for
	 * @return true if it has that tag
	 */
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	/**
	 * Checks if this result has all specified tags.
	 * 
	 * @param tags
	 *            tags to check for
	 * @return true if result contains all those tags
	 */

	public boolean hasTags(List<String> tags) {
		boolean isValid = true;
		for (String tag : tags)
			if (!this.tags.contains(tag))
				isValid = false;
		return isValid;
	}

	/**
	 * Checks if this result has all specified tags.
	 * 
	 * @param tags
	 *            tags to check for
	 * @return true if result contains all those tags
	 */

	public boolean hasTags(String[] tags) {
		return hasTags(new ArrayList<String>(Arrays.asList(tags)));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Taggable other = (Taggable) obj;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		return result;
	}

}
