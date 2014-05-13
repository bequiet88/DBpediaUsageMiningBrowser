package filters;

import java.util.ArrayList;
import java.util.List;

import ldb.RDFTriple;
import result.Result;
import result.ResultComposite;
import result.Table;
import filter.FilterTable;

/**
 * Splits a table in two halfs.
 * 
 */
public class HalfFilter extends FilterTable {

	@Override
	public Result execute(Table table) {
		int halfSize = table.size() / 2;
		List<RDFTriple> firstHalf = new ArrayList<RDFTriple>(halfSize);
		List<RDFTriple> secondHalf = new ArrayList<RDFTriple>(halfSize);

		// split table into two lists
		for (int i = 0; i < halfSize; i++) {
			firstHalf.add(table.getTriples().get(i));
			secondHalf.add(table.getTriples().get(i + halfSize));
		}
		// if the table has an odd number of entries
		if (table.size() % 2 != 0)
			// add last entry to second half
			secondHalf.add(table.getTriples().get(table.size() - 1));

		// create ResultComponent from the two tables
		ResultComposite resultComponent = new ResultComposite();
		resultComponent.add(new Table(firstHalf));
		resultComponent.add(new Table(secondHalf));

		return resultComponent;
	}
}
