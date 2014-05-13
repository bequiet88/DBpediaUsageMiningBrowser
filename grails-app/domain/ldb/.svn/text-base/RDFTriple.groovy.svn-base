package ldb

import result.Metadata

/**
 * Simple class to hold an RDF triple of a subject, a predicate and an object
 */

class RDFTriple implements Serializable, Metadata{
	private static final long serialVersionUID = 1L;


	protected Map<String, String> metadata = new HashMap<String, String>();


	public Map<String, String> getMetadata() {
		return metadata;
	}

	/**
	 * Adds the name/value pair to the metadata.
	 *
	 * @param name
	 *            name of the attribute to add
	 * @param value
	 *            value of the attribute to add
	 */
	@Override
	public void addMetaData(String name, String value) {
		metadata.put(name, value);
	}

	/**
	 * Looks for the value for a given attribute in the metadata.
	 *
	 * @param name
	 *            name of the attribute to look for
	 * @return the respective value or null if not found
	 */

	@Override
	public String getMetadata(String name) {
		return metadata.get(name);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((pred == null) ? 0 : pred.hashCode());
		result = prime * result + ((sub == null) ? 0 : sub.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this.is(obj))
			return true;
		if (obj.is(null))
			return false;
		if (!getClass().is(obj.getClass()))
			return false;
		RDFTriple other = (RDFTriple) obj;
		if (object.is(null)) {
			if (!other.object.is(null))
				return false;
		} else if (object != other.object)
			return false;
		if (pred.is(null)) {
			if (!other.pred.is(null))
				return false;
		} else if (pred != other.pred)
			return false;
		if (sub.is(null)) {
			if (!other.sub.is(null))
				return false;
		} else if (sub !=other.sub)
			return false;
		return true;
	}


	private String sub
	private String pred
	private String object


	RDFTriple(sub, pred, obj){
		this.sub = sub
		this.pred = pred
		this.object = obj
	}

	public String getSub() {
		return sub;
	}

	public String getPred() {
		return pred;
	}

	public String getObj() {
		return object;
	}


	static constraints = {
	}
}
