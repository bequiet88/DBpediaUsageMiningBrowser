package request;

import java.util.List;

/**
 * simple class for holding a tagName/indices pair; used to describe that this tagName should be given to the children
 * of a result, whose index is contained in indices
 * 
 */

public class AddTagParameter {

	private String tagName;
	private List<Integer> indices;

	public AddTagParameter(String tagName, List<Integer> indices) {
		this.tagName = tagName;
		this.indices = indices;
	}

	public String getTagName() {
		return tagName;
	}

	public List<Integer> getIndices() {
		return indices;
	}

	public void addIndex(int index) {
		if (!indices.contains(index))
			indices.add(index);
	}
}
