package filters;

import java.util.ArrayList;
import java.util.List;

import ldb.RDFTriple;
import result.Result;
import result.Table;
import filter.FilterTable;

/**
 * Duplicates all entries in a table.
 * 
 * Mainly for testing deduplication.
 * 
 */
public class DuplicateFilter extends FilterTable {

	@Override
	public Result execute(Table table) {

		List<RDFTriple> duplicated = new ArrayList<RDFTriple>();
		for (RDFTriple triple : table.getTriples()) {
			duplicated.add(triple);
			duplicated.add(triple);
		}
		return new Table(duplicated);
	}
}
