package utils;

import org.jdom2.Element;

import result.Result;

/**
 * compares tags of a result to requirements, which are defined in <tags>-Element of a <filter>-Element
 * 
 */
public class TagValidator {

	/**
	 * prevent instantiation
	 */
	private TagValidator() {
	}

	/**
	 * decides whether the tags of a results fulfill the requirements defined in the tagsElement of a filter
	 * 
	 * @param result
	 *            result, which shall or shall not be filtered according to the requirements
	 * @param tagsElement
	 *            xml Element, which contains the requirements. For example if the result has to have the tag tag1 and
	 *            not the tags tag2 or tag3 the format would be: <tags op="and"> <tag>tag1><tag> <not> <tags op="or">
	 *            <tag>tag2<tag> <tag>tag3<tag> </tags> </not> </tags>
	 * 
	 */
	public static boolean matches(Result result, Element tagsElement) {

		if (tagsElement == null)
			return true;

		if (result == null)
			return false;

		if (tagsElement.getName().equals("not")) {
			return !matches(result, tagsElement.getChildren().get(0));
		}

		if (tagsElement.getName().equals("tag")) {
			return result.hasTag(tagsElement.getText());
		}

		String op = tagsElement.getAttributeValue("op");

		if (op.equals("and")) {
			boolean matches = true;
			for (Element child : tagsElement.getChildren()) {
				if (!matches(result, child))
					matches = false;
			}
			return matches;
		}

		if (op.equals("or")) {
			boolean matches = false;
			for (Element child : tagsElement.getChildren()) {
				if (matches(result, child))
					matches = true;
			}
			return matches;
		}

		return false;
	}

}
