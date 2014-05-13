package filters;

import result.Result;
import result.ResultComposite;
import result.Table;
import filter.Filter;
import filter.FilterTable;

/**
 * Splits the table into one part with coordinates and one part with everything else.
 * 
 */
public class CoordinatesAndNormal extends FilterTable {

	@Override
	public Result execute(Table table) {
		Filter coordinatesFilter = new CoordinatesFilter();
		Table coordinates = (Table) coordinatesFilter.execute(table);
		coordinates.setCaption("Coordinates:");

		ResultComposite rc = new ResultComposite();
		rc.add(coordinates);

		Table noCoordinates = new Table(table.getTriples());
		noCoordinates.setCaption("Normal:");
		noCoordinates.addTag("NoCoordinates");
		rc.add(noCoordinates);

		return rc;
	}

}
