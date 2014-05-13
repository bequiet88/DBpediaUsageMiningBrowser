package filters;

import java.util.ArrayList;
import java.util.List;

import result.Result;
import result.ResultComposite;
import filter.FilterComposite;

/**
 * Removes all clusters with the given tag from the tree.
 * 
 */
public class RemoveClusterFilter extends FilterComposite {
	/**
	 * tag of clusters to remove
	 */
	protected String removeTag;

	public void setRemoveTag(String tag) {
		removeTag = tag;
	}

	@Override
	public Result execute(ResultComposite composite) {
		if (composite.hasTag(removeTag))
			return null;

		removeResult(composite);
		return composite;
	}

	private void removeResult(Result result) {

		List<Result> childrenToRemove = new ArrayList<Result>();

		for (Result child : result.getChildren())
			if (child.hasTag(removeTag))
				childrenToRemove.add(child);
			else
				removeResult(child);

		result.getChildren().removeAll(childrenToRemove);
	}

}
