package filters;

import java.util.ArrayList;
import java.util.List;

import ldb.RDFTriple;
import result.Result;
import result.Table;
import filter.FilterTable;

/**
 * Removes duplicates from the table.
 * 
 */
public class DeduplicateFilter extends FilterTable {

	@Override
	public Result execute(Table table) {
		List<RDFTriple> deduplicated = new ArrayList<RDFTriple>();
		for (RDFTriple triple : table.getTriples()) {
			if (!deduplicated.contains(triple))
				deduplicated.add(triple);
		}
		return new Table(deduplicated);
	}
}
