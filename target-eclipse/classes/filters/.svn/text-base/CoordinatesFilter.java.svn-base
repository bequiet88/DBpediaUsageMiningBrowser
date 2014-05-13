package filters;

import ldb.RDFTriple;

/**
 * Filters out triples which represent coordinates.
 * 
 */
public class CoordinatesFilter extends SelectionFilter {

	/**
	 * list of known coordinate predicates
	 */
	private final static String[] coordinateURIs = new String[] { "http://www.w3.org/2003/01/geo/wgs84_pos#geometry",
			"http://www.georss.org/georss/point", "http://www.w3.org/2003/01/geo/wgs84_pos#long",
			"http://www.w3.org/2003/01/geo/wgs84_pos#lat" };

	@Override
	protected boolean fits(RDFTriple triple) {
		for (String coordinateURI : coordinateURIs)
			if (triple.getPred().startsWith(coordinateURI))
				return true;
		return false;
	}

	@Override
	protected String[] tags() {
		return new String[] { "Coordinate" };
	}

}
