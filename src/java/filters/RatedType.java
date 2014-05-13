package filters;

public class RatedType implements Comparable<RatedType> {
	private String uri;
	private Float rating;
	
	public RatedType(String uri, float rating) {
		this.uri = uri;
		this.rating = rating;
	}
	
	public String getURI() {
		return uri;
	}
	
	public void setURI(String uri) {
		this.uri = uri;
	}
	
	public float getRating() {
		return rating;
	}
	
	public void setRating(float rating) {
		this.rating = rating;
	}
	
	public String toString() {
		return uri + " (" + rating + ")";
	}
	
	@Override
	public int compareTo(RatedType other) {
		return rating.compareTo(other.rating);
	}
}

