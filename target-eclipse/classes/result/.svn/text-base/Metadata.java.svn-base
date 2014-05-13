package result;

import java.util.Map;

/**
 * Interface for adding metadata as string string pairs.
 * 
 */
public interface Metadata {

	/**
	 * Returns the whole internal map.
	 * 
	 * @return the whole map
	 */
	public abstract Map<String, String> getMetadata();

	/**
	 * Adds the name/value pair to the metadata.
	 * 
	 * @param name
	 *            name of the attribute to add
	 * @param value
	 *            value of the attribute to add
	 */
	public abstract void addMetaData(String name, String value);

	/**
	 * Looks for the value for a given attribute in the metadata.
	 * 
	 * @param name
	 *            name of the attribute to look for
	 * @return the respective value or null if not found
	 */

	public abstract String getMetadata(String name);

}