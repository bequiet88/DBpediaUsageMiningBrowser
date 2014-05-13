package filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ldb.RDFTriple;
import result.Result;
import result.Table;
import filter.FilterTable;

/**
 * Randomly assigns a credibility rating between 0-100 to all triples in the table.
 * 
 */
public class MetadataRandomCredibilityFilter extends FilterTable {

	@Override
	public Result execute(Table table) {

		List<RDFTriple> metaDataTriples = new ArrayList<RDFTriple>();
		for (RDFTriple triple : table.getTriples()) {
			Random r = new Random();
			int cred = r.nextInt(101);
			String credibility = Integer.toString(cred);
			triple.addMetaData("credibility", credibility);
			metaDataTriples.add(triple);
		}
		return new Table(metaDataTriples);
	}

}
