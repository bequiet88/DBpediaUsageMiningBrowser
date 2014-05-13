package filters;

import java.util.ArrayList;
import java.util.List;

import ldb.RDFTriple;
import result.Result;
import result.ResultComposite;
import result.Table;
import de.unimannheim.dws.controller.ClusterGrouperController;
import de.unimannheim.dws.model.ExchangeRDFTriple;
import filter.FilterTable;

/**
 * Groups results into clusters 
 * 
 */
public class ClusterGrouperFilter extends FilterTable {

	
	private String clusterer = "";
	
	public void setClusterer(String clusterer) {
		this.clusterer = clusterer;
	}

	@Override
	public Result execute(Table table) {

		String[] options = { "-O", "-C", clusterer, "-P", "-R",
		"7" };
		
		List<ExchangeRDFTriple> interfaceList = new ArrayList<ExchangeRDFTriple>();

		for (RDFTriple rdfTriple : table.getTriples()) {
			interfaceList.add(new ExchangeRDFTriple(rdfTriple.getSub(),
					rdfTriple.getPred(), rdfTriple.getObj(), ""));
		}

		interfaceList = ClusterGrouperController
				.readObjectPropertyPairsClusterList(interfaceList, options,
						this.processedResource);

		ResultComposite resultComponent = new ResultComposite();
		Table cluster = null;
		List<RDFTriple> clusterList = new ArrayList<RDFTriple>();

		if (interfaceList.size() > 0) {
			String groupCache = interfaceList.get(0).getGroup();
			for (int i = 0; i < interfaceList.size(); i++) {			
				ExchangeRDFTriple current = interfaceList.get(i);
				if (current.getGroup().equals(groupCache)) {
					// insert current triple
					clusterList.add(new RDFTriple(current.getSub(), current
							.getPred(), current.getObj()));
					groupCache = current.getGroup();
				} else {
					// Append cluster to result composite
					cluster = new Table(clusterList);
					cluster.setCaption(groupCache);
					resultComponent.add(cluster);
					
					// Empty cluster table for next cluster
					cluster = null;
					clusterList = new ArrayList<RDFTriple>();
					
					// Insert first triple of new cluster
					clusterList.add(new RDFTriple(current.getSub(), current
							.getPred(), current.getObj()));
					groupCache = current.getGroup();
				}

			}

		}
		return resultComponent;

	}
}
