package request;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import utils.NameValue;
import utils.Utils;

/**
 * Reads in the configuration of one filter and makes it available.
 * 
 */
class FilterConfigReader {

	private String className;
	private String caption;

	private ArrayList<NameValue> parameters;
	private Element tagsElement;

	private ArrayList<AddTagParameter> addTags;

	private Element currentFilter;
	private ListIterator<Element> iterator;

	/**
	 * Creates a reader with respect to one XML file.
	 * 
	 * MUST CALL NEXT BEFOR FIRST USE
	 * 
	 * @param fileName
	 */

	public FilterConfigReader(String fileName) {
		Document doc;
		try {
			// validate xml file against filters.xsd (I did not find out how to validate a xml file with JDom2 against a
			// local xml schema which is not declared in the specific xml file, so I used javax.xml)
			// A developer definitely does not want to write this header (as in filters4.xml):
			// xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			// xsi:noNamespaceSchemaLocation="filters.xsd"
			// into his xml filter file.

			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			File schemaLocation = Utils.getFile("filters.xsd");
			if (!schemaLocation.exists())
				throw new RuntimeException("Schema file " + schemaLocation.getAbsolutePath() + " not found");
			Schema schema = factory.newSchema(schemaLocation);
			Validator validator = schema.newValidator();
			Source source = new StreamSource(Utils.getURL(fileName).toString());
			validator.validate(source);

			// actually build file
			doc = new SAXBuilder().build(Utils.getURL(fileName));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		iterator = doc.getRootElement().getChildren().listIterator();
	}

	public String getClassName() {
		return className;
	}

	public String getCaption() {
		return caption;
	}

	public ArrayList<NameValue> getParameters() {
		return parameters;
	}

	public Element getTagsElement() {
		return tagsElement;
	}

	public ArrayList<AddTagParameter> getAddTags() {
		return addTags;
	}

	/**
	 * Goes to the next filter.
	 */
	public void next() {
		currentFilter = iterator.next();
		className = currentFilter.getAttributeValue("classname");
		caption = currentFilter.getAttributeValue("caption");

		parameters = readParameters(currentFilter);
		tagsElement = currentFilter.getChild("tags");
		addTags = readAddTags(currentFilter);
	}

	/**
	 * checks if there are further elements to iterate over
	 * 
	 * @return true iff there is another element
	 */

	public boolean hasNext() {
		return iterator.hasNext();
	}

	/**
	 * Reads in addTags from XML file.
	 * 
	 * @param e
	 *            filter to read addTags for
	 * 
	 * @return list of AddTagParameter
	 */

	private ArrayList<AddTagParameter> readAddTags(Element e) {
		Element addTags = e.getChild("addTags");

		if (addTags == null) {
			return new ArrayList<AddTagParameter>();
		}

		List<Element> addTagsChildren = addTags.getChildren();

		if (addTagsChildren == null) {
			return new ArrayList<AddTagParameter>();
		}

		ArrayList<AddTagParameter> result = new ArrayList<AddTagParameter>();
		for (Element child : addTagsChildren) {
			if (child.getChildren().size() != 0)
				throw new RuntimeException("Tags have to be attached to children represented by an integer");
			else {
				String tag = child.getAttribute("name").getValue();
				try {
					int son = Integer.parseInt(child.getAttribute("index").getValue());
					boolean containsTag = false;
					for (AddTagParameter r : result) {
						if (r.getTagName() == tag) {
							r.addIndex(son);
							containsTag = true;
							break;
						}
					}
					if (!containsTag) {
						ArrayList<Integer> temp = new ArrayList<Integer>();
						temp.add(son);
						result.add(new AddTagParameter(tag, temp));
					}
				} catch (NumberFormatException exception) {
					throw new RuntimeException("Tags have to be attached to children represented by an integer");
				}

			}
		}

		return result;
	}

	/**
	 * Reads in parameters from the XML .
	 * 
	 * format HAS TO BE
	 * 
	 * <Parameters> <parName1>val1</parName1> <parName2>val2</parName2> <parName3>val3</parName3> ... </Parameters>
	 * 
	 * else setDefault() will be called which probably does not exist
	 * 
	 * @param e
	 *            filter to read parameters for
	 * @return list of parameters
	 */
	private static ArrayList<NameValue> readParameters(Element e) {
		ArrayList<NameValue> parameters = new ArrayList<NameValue>();

		String singleParameter = e.getAttributeValue("parameters");

		Element parameterElement = e.getChild("parameters");
		if (parameterElement != null)
			for (Element p : parameterElement.getChildren())
				parameters.add(new NameValue(p.getAttribute("name").getValue(), p.getAttribute("value").getValue()));
		else if (singleParameter != null)
			parameters.add(new NameValue("default", singleParameter));

		return parameters;
	}

}
