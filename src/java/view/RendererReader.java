package view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import result.Result;
import result.Table;
import utils.NameValue;
import utils.TagValidator;
import utils.Utils;

/**
 * Reads in the renderer configuration and makes it available as a list.
 * 
 */
public class RendererReader {

	private List<RendererEntry> rendererEntries = new ArrayList<RendererEntry>();

	private Element root;

	public List<RendererEntry> getRendererEntries() {
		return rendererEntries;

	}

	public RendererReader(String filename, Result result) {

		Document doc;
		try {
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			File schemaLocation = Utils.getFile("renderers.xsd");
			Schema schema = factory.newSchema(schemaLocation);
			Validator validator = schema.newValidator();
			Source source = new StreamSource(Utils.getURL(filename).toString());
			validator.validate(source);

			doc = new SAXBuilder().build(Utils.getURL(filename));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		root = doc.getRootElement();

		run(result);

	}

	/**
	 * Recursive function for finding all results and their respective renderers.
	 * 
	 * @param result
	 *            result to find renderer(s) for
	 */
	private void run(Result result) {

		List<NameValue> renderers = getMatchingRenderers(result);

		if (renderers.isEmpty())
			if (result instanceof Table) {
				String defaultRenderer;
				defaultRenderer = getDefaultRenderer();
				rendererEntries.add(new RendererEntry(defaultRenderer, result, result.getCaption()));
			} else {
				rendererEntries.add(new RendererEntry(null, null, result.getCaption(), true, false));
				for (Result r : result.getChildren())
					run(r);
				rendererEntries.add(new RendererEntry(null, null, null, false, true));
			}
		else {
			if (renderers.size() == 1) {

				if (renderers.get(0).getVal().equals("composite")) {
					rendererEntries.add(new RendererEntry(renderers.get(0).getName(), result, result.getCaption()));
				} else {
					runTargetTypeTableRenderer(result, renderers.get(0).getName());
				}

			} else {
				// new Cluster
				rendererEntries.add(new RendererEntry(null, null, result.getCaption(), true, false));

				// only first one needs caption
				result.setCaption(null);

				if (renderers.get(0).getVal().equals("composite")) {
					rendererEntries.add(new RendererEntry(renderers.get(0).getName(), result, null));
				} else {
					runTargetTypeTableRenderer(result, renderers.get(0).getName());
				}

				renderers.remove(0);

				// result gets rendered on the same level by all matching renderers
				for (NameValue renderer : renderers) {
					if (renderer.getVal().equals("composite"))
						rendererEntries.add(new RendererEntry(renderer.getName(), result, null));
					else
						runTargetTypeTableRenderer(result, renderer.getName());

				}

				rendererEntries.add(new RendererEntry(null, null, null, false, true));

			}

		}
	}

	/**
	 * Responsible for renderers with targettype table.
	 * 
	 * Recursively goes to the leafs of the given result tree and assign the renderer to all tables.
	 */
	private void runTargetTypeTableRenderer(Result result, String renderer) {
		if (result instanceof Table)
			rendererEntries.add(new RendererEntry(renderer, result, result.getCaption()));
		else {
			rendererEntries.add(new RendererEntry(null, null, result.getCaption(), true, false));
			for (Result r : result.getChildren()) {
				runTargetTypeTableRenderer(r, renderer);
			}
			rendererEntries.add(new RendererEntry(null, null, null, false, true));
		}

	}

	/**
	 * Returns the default renderer.
	 * 
	 * @return the custom default renderer or JavaDefaultTableRenderer if not set
	 */
	private String getDefaultRenderer() {
		Element defaultRenderer = root.getChild("defaultRenderer");
		return defaultRenderer != null ? defaultRenderer.getAttribute("name").getValue() : "JavaDefaultTableRenderer";
	}

	/**
	 * Looks for renderers that match the given results tags.
	 * 
	 * @param result
	 *            all renderers that fit the given renderers tags
	 */
	private List<NameValue> getMatchingRenderers(Result result) {
		List<NameValue> list = new ArrayList<NameValue>();
		for (Element e : root.getChildren("renderer"))
			if (TagValidator.matches(result, e.getChild("tags")))
				list.add(new NameValue(e.getAttributeValue("name"), e.getAttributeValue("targetType")));
		return list;
	}

}
