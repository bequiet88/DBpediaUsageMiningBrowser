package view;

import result.Result;

/**
 * Entry for one renderer - result pair.
 * 
 * To be used by the view.
 * 
 */
public class RendererEntry {
	private String name;
	private Result result;
	private String caption;
	private boolean clusterStart = false;
	private boolean clusterEnd = false;

	public RendererEntry(String name, Result result, String caption, boolean clusterStart, boolean clusterEnd) {
		super();
		this.name = name;
		this.result = result;
		this.caption = caption;
		this.clusterStart = clusterStart;
		this.clusterEnd = clusterEnd;
	}

	public String getName() {
		return name;
	}

	public Result getResult() {
		return result;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public boolean isClusterStart() {
		return clusterStart;
	}

	public boolean isClusterEnd() {
		return clusterEnd;
	}

	public RendererEntry(String name, Result result, String caption) {
		super();
		this.name = name;
		this.result = result;
		this.caption = caption;
	}

}
