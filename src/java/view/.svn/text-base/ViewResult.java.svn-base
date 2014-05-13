package view;

import java.util.ArrayList;
import java.util.List;

import ldb.RDFTriple;
import result.Table;

/**
 * class for the view to visualize the results in a tree form
 * 
 */

public class ViewResult {

	private boolean clusterStart, clusterEnd;
	private String caption;
	private List<RDFTriple> tripleList;

	public ViewResult(boolean clusterStart, boolean clusterEnd, String caption, Table table) {
		this.clusterStart = clusterStart;
		this.clusterEnd = clusterEnd;
		this.caption = caption;

		if (table != null) {
			this.tripleList = table.getTriples();
		} else
			this.tripleList = null;
	}

	public String getCaption() {
		return caption;
	}

	public boolean getClusterEnd() {
		return clusterEnd;
	}

	public boolean getClusterStart() {
		return clusterStart;
	}

	public List<RDFTriple> getTable() {
		return tripleList;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public void setClusterEnd(boolean clusterEnd) {
		this.clusterEnd = clusterEnd;
	}

	public void setClusterStart(boolean clusterStart) {
		this.clusterStart = clusterStart;
	}

	public void setTable(ArrayList<RDFTriple> table) {
		this.tripleList = table;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result + (clusterEnd ? 1231 : 1237);
		result = prime * result + (clusterStart ? 1231 : 1237);
		result = prime * result + ((tripleList == null) ? 0 : tripleList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ViewResult other = (ViewResult) obj;
		if (caption == null) {
			if (other.caption != null)
				return false;
		} else if (!caption.equals(other.caption))
			return false;
		if (clusterEnd != other.clusterEnd)
			return false;
		if (clusterStart != other.clusterStart)
			return false;
		if (tripleList == null) {
			if (other.tripleList != null)
				return false;
		} else if (!tripleList.equals(other.tripleList))
			return false;
		return true;
	}
}
