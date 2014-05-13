package filters;

import ldb.QueryController;
import ldb.RDFTriple;
import result.Result;
import result.Table;
import filter.FilterTable;

/**
 * Filter for testing canceling of queries.
 * 
 */
public class SchaefchenFilter extends FilterTable {

	@Override
	public Result execute(Table table) {
		System.out.println("1 Schäfchen springt über den Zaun");
		int i = 1;
		for (;;) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Beim Schäfchen zählen unterbrochen!");
			}
			i++;
			System.out.println(i - 1 + " Schäfchen springen über den Zaun");
			if (QueryController.isRequestCancelled())
				return new Table(new RDFTriple[] { new RDFTriple(i - 1 + " Schäfchen", "sind gesprungen",
						"über den Zaun") });
		}
	}
}
