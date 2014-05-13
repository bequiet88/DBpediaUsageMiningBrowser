package filters;

import java.util.ArrayList;
import java.util.List;

import ldb.RDFTriple;
import result.Result;
import result.ResultComposite;
import result.Table;
import filter.FilterTable;

/**
 * Groups results into clusters of the size specified by entriesPerPage.
 * 
 */
public class ClusterFilter extends FilterTable {

	/**
	 * number of Results per cluster
	 */
	protected int entriesPerPage = Integer.MAX_VALUE;

	@Override
	public Result execute(Table table) {
		List<RDFTriple> solutions = table.getTriples();
		int size = solutions.size();
		ResultComposite resultComponent = new ResultComposite();
		int currentCluster = 0;
		OuterLoop: while (true) {
			List<RDFTriple> cluster = new ArrayList<RDFTriple>(); // create new cluster
			for (int i = 0; i < entriesPerPage; i++)
				if (entriesPerPage * currentCluster + i >= size) { // make sure we stay within bounds
					if (cluster.size() > 0)
						resultComponent.add(new Table(cluster));
					break OuterLoop; // break if end is reached
				} else
					cluster.add(solutions.get(currentCluster * entriesPerPage + i)); // else keep adding entries

			resultComponent.add(new Table(cluster)); // add cluster to end results
			currentCluster++;
		}
		/*
		// enumerate clusters
		for (int i = 0; i < resultComponent.size(); i++)
			resultComponent.get(i).setCaption("Cluster " + (i + 1));
		*/

		return resultComponent;

	}

	public void setEntriesPerPage(int entriesPerPage) {
		this.entriesPerPage = entriesPerPage;
	}

}
