package filters;

import java.util.ArrayList;
import java.util.List;

import ldb.RDFTriple;
import result.Result;
import result.ResultComposite;
import result.Table;
import de.unimannheim.dws.model.ExchangeRDFTriple;
import de.unimannheim.dws.controller.SimpleCountController;
import filter.FilterTable;

/**
 * Groups results into clusters 
 * 
 */
public class SimpleCounterFilter extends FilterTable {

	@Override
	public Result execute(Table table) {

		String[] options = {"-S","frequency","-N","3","-R"};
		
		List<ExchangeRDFTriple> interfaceList = new ArrayList<ExchangeRDFTriple>();

		for (RDFTriple rdfTriple : table.getTriples()) {
			interfaceList.add(new ExchangeRDFTriple(rdfTriple.getSub(),
					rdfTriple.getPred(), rdfTriple.getObj(), ""));
		}

		interfaceList = SimpleCountController
				.readObjectClassPropertyPairsList(interfaceList, options,
						this.processedResource);

		ResultComposite resultComponent = new ResultComposite();
		Table cluster = null;
		List<RDFTriple> clusterList = new ArrayList<RDFTriple>();

		if (interfaceList.size() > 0) {
			String groupCache = "Group " + interfaceList.get(0).getGroup();
			for (int i = 0; i < interfaceList.size(); i++) {			
				ExchangeRDFTriple current = interfaceList.get(i);
				if (("Group " + current.getGroup()).equals(groupCache)) {
					// insert current triple
					clusterList.add(new RDFTriple(current.getSub(), current
							.getPred(), current.getObj()));
					groupCache = "Group " + current.getGroup();
				} else {
					// Append cluster to result composite
					cluster = new Table(clusterList);
					cluster.setCaption(groupCache);
					resultComponent.add(cluster);
					
					// Empty cluster table for next bin
					cluster = null;
					clusterList = new ArrayList<RDFTriple>();
					
					// Insert first triple of new bin
					clusterList.add(new RDFTriple(current.getSub(), current
							.getPred(), current.getObj()));
					groupCache = "Group " + current.getGroup();
				}

			}

		}
		return resultComponent;

	}
}
