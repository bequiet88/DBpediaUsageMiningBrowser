package utils;

/**
 * Simple class for holding an attribute/value pair.
 * 
 */

public class NameValue {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((val == null) ? 0 : val.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NameValue other = (NameValue) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (val == null) {
			if (other.val != null)
				return false;
		} else if (!val.equals(other.val))
			return false;
		return true;
	}

	private String name;
	private String val;

	public NameValue(String name, String val) {
		super();
		this.name = name;
		this.val = val;
	}

	public String getVal() {
		return val;
	}

	public String getName() {
		return name;
	}

}
