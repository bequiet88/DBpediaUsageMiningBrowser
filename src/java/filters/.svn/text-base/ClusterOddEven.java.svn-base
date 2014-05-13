package filters;

import result.Result;
import result.ResultComposite;
import result.Table;
import filter.FilterTable;

/**
 * Runs the regular ClusterFilter and tags the resulting clusters as odd and even.
 * 
 */
public class ClusterOddEven extends FilterTable {

	protected int entriesPerPage = Integer.MAX_VALUE;

	public void setEntriesPerPage(int entriesPerPage) {
		this.entriesPerPage = entriesPerPage;
	}

	@Override
	public Result execute(Table table) {
		ClusterFilter cf = new ClusterFilter();
		cf.setEntriesPerPage(entriesPerPage);

		// cast is okay because clustering always returns ResultComponent
		ResultComposite resultComponents = (ResultComposite) cf.execute(table);

		for (int i = 0; i < resultComponents.size(); i++)
			if (i % 2 == 0)
				resultComponents.get(i).addTag("even");
			else
				resultComponents.get(i).addTag(("odd"));

		return resultComponents;
	}

}
