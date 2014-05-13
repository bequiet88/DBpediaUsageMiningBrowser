package filters;

import java.util.ArrayList;
import java.util.List;

import result.Result;
import result.ResultComposite;
import result.Table;
import filter.FilterComposite;

/**
 * Prunes a tree by removing all empty components and removing intermediate clusters without siblings.
 * 
 */
public class PruneTreeFilter extends FilterComposite {

	@Override
	public Result execute(ResultComposite composite) {
		prune(composite);

		return trim(composite);
	}

	/**
	 * remove empty results
	 */
	private void prune(Result composite) {

		List<Result> childrenToRemove = new ArrayList<Result>();

		for (Result child : composite.getChildren())
			if (child.size() == 0)
				childrenToRemove.add(child);
			else
				prune(child);

		composite.getChildren().removeAll(childrenToRemove);

	}

	/**
	 * remove intermediate results
	 */

	private Result trim(Result result) {
		if (result instanceof Table)
			return result;
		ResultComposite composite = (ResultComposite) result;

		if (composite.size() == 1)
			return trim(composite.get(0));

		List<Result> newChildren = new ArrayList<Result>();
		for (Result child : composite.getChildren()) {
			newChildren.add(trim(child));
		}
		composite.getChildren().clear();
		composite.getChildren().addAll(newChildren);

		return composite;
	}

}
