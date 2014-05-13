package view;

/**
 * Class for holding a resource name and its latitude and longitude.
 * 
 */
public class Coordinate {

	private final String resName;
	private final double latitude, longitude;

	public String getResName() {
		return resName;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public Coordinate(String resName, double latitude, double longitude) {
		super();
		this.resName = resName;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((resName == null) ? 0 : resName.hashCode());
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
		Coordinate other = (Coordinate) obj;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		if (resName == null) {
			if (other.resName != null)
				return false;
		} else if (!resName.equals(other.resName))
			return false;
		return true;
	}
}
