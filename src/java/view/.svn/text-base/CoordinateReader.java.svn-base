package view;

import java.util.ArrayList;
import java.util.List;

import ldb.RDFTriple;
import result.Table;
import utils.Utils;

/**
 * 
 * Reads coordinates for resources from a table and returns them as a list of Coordinate.
 * 
 */
public class CoordinateReader {

	private Table table;

	private List<Coordinate> coordinates = new ArrayList<Coordinate>();

	private List<String> resources = new ArrayList<String>();

	public CoordinateReader(Table table) {
		this.table = table;
	}

	public List<Coordinate> readCoordinates() {

		// create a list of all resources in the table
		for (RDFTriple triple : table.getTriples())
			if (!resources.contains(triple.getSub()))
				// add each resource only once, we don't want duplicates right now
				resources.add(triple.getSub());

		// now get one coordinate for each resource
		for (String resource : resources)
			if (readCoordinate(resource) != null)
				coordinates.add(readCoordinate(resource));

		return coordinates;

	}

	/**
	 * Tries to read in coordinates for this resource by calling all known readers.
	 * 
	 * @param resource
	 *            resource to read coordinates for
	 * @return respective coordinates or null if unsuccessful
	 */

	private Coordinate readCoordinate(String resource) {

		if (readGeorssPoint(resource) != null)
			return readGeorssPoint(resource);

		if (readW3GeoWgs84_pos_lat_long(resource) != null)
			return readW3GeoWgs84_pos_lat_long(resource);

		if (readW3GeoWgs84_pos_geometry(resource) != null)
			return readW3GeoWgs84_pos_geometry(resource);

		return null;

	}

	/**
	 * Reads in http://www.w3.org/2003/01/geo/wgs84_pos#geometry
	 */
	private Coordinate readW3GeoWgs84_pos_geometry(String resource) {
		RDFTriple triple = Utils.getBySubPred(table, resource, "http://www.w3.org/2003/01/geo/wgs84_pos#geometry");
		if (triple == null)
			return null;

		String coordInfo = triple.getObj();

		String latitude = coordInfo.substring(coordInfo.indexOf(" ") + 1, coordInfo.indexOf(")"));
		String longitude = coordInfo.substring(6, coordInfo.indexOf(" "));

		return new Coordinate(resource, Double.parseDouble(latitude), Double.parseDouble(longitude));
	}

	/**
	 * Reads in http://www.w3.org/2003/01/geo/wgs84_pos#lat and http://www.w3.org/2003/01/geo/wgs84_pos#long
	 */
	private Coordinate readW3GeoWgs84_pos_lat_long(String resource) {
		RDFTriple latTriple = Utils.getBySubPred(table, resource, "http://www.w3.org/2003/01/geo/wgs84_pos#lat");
		RDFTriple longTriple = Utils.getBySubPred(table, resource, "http://www.w3.org/2003/01/geo/wgs84_pos#long");
		if (latTriple == null || longTriple == null)
			return null;

		String latInfo = latTriple.getObj();
		String longInfo = longTriple.getObj();

		String latitude = latInfo.substring(0, latInfo.indexOf("^^"));
		String longitude = longInfo.substring(0, longInfo.indexOf("^^"));

		return new Coordinate(resource, Double.parseDouble(latitude), Double.parseDouble(longitude));

	}

	/**
	 * Reads in http://www.georss.org/georss/point
	 */

	private Coordinate readGeorssPoint(String resource) {
		RDFTriple triple = Utils.getBySubPred(table, resource, "http://www.georss.org/georss/point");
		if (triple == null)
			return null;

		String coordInfo = triple.getObj();

		String latitude = coordInfo.substring(0, coordInfo.indexOf(" "));
		String longitude = coordInfo.substring(coordInfo.indexOf(" ") + 1, coordInfo.indexOf("@"));

		return new Coordinate(resource, Double.parseDouble(latitude), Double.parseDouble(longitude));
	}

}
