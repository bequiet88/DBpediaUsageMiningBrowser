package request;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.WordUtils;

import result.Result;
import result.ResultComposite;
import utils.AppConfig;
import utils.Debug;
import utils.NameValue;
import utils.Serialization;
import utils.TagValidator;
import utils.Utils;
import filter.Filter;

/**
 * Main class for handling requests.
 * 
 * Gets the data from the server, reads in XML configuration, sets meta data, runs filters ...
 * 
 * 
 */
public class QueryHandler {

	private Request request;

	/**
	 * The request string that was entered.
	 */
	private String requestString;

	/**
	 * The actual URI that will be queried.
	 */
	private String requestURI;

	/**
	 * True if an error occurred during the request.
	 */
	private boolean errorOccurred = false;

	/**
	 * Collects all error messages received during the request.
	 */

	private String errorMessage = "";

	public String getRequestString() {
		return requestString;
	}

	/**
	 * The root of the result tree.
	 */

	private Result root = null;

	/**
	 * Constructor for QueryHandler.
	 * 
	 * Doesn't do anything further. Use run() to start the actual process.
	 * 
	 * @param request
	 * 
	 *            the request for this QueryHandler
	 */

	public QueryHandler(Request request) {
		Debug.printDelay("QueryHandler Konstruktor");
		this.request = request;
		this.requestString = request.getRequestURI();
	}

	public QueryHandler(String requestURI) {
		Debug.printDelay("QueryHandler Konstruktor");
		this.request = new Request(requestURI);
		this.requestString = requestURI;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Returns the result of this request i.e. the root of the saved result tree.
	 * 
	 * Run the handler first or this will be null.
	 * 
	 * @return root of the request tree or null if query hasn't run yet.
	 */

	public Result getResult() {
		return root;
	}

	/**
	 * Starts the process of retrieving the data and filtering it.
	 * 
	 * @return
	 */
	public Result run() {
		Debug.printDelay("begin run()");
		try {
			requestURI = Utils.unicodeToJenaString(requestString);
			if (AppConfig.isLocalMode()) {
				if (AppConfig.isLocalModeWithRDF()) {
					RequestLocal requestLocal = new RequestLocal(requestString);
					requestLocal.runQuery();
					root = requestLocal.getResult();
				} else
					root = (Result) Serialization.loadResult(requestURI, true);
			} else {
				request.runQuery();
				root = request.getResult();
			}
		} catch (Exception e) {
			if (AppConfig.isDebugMode())
				throw new RuntimeException(e);
			setError("External error: " + e.getMessage());
		}

		if (!(errorOccurred)) {
			try {
				applyFilters(AppConfig.getFilterFile());
			} catch (Exception e) {
				if (AppConfig.isDebugMode())
					throw new RuntimeException(e);
				setError("Internal error: " + e.getMessage());
			}
		}
		Debug.printDelay("end run()");
		return root;
	}

	/**
	 * Reads the filter list from the specified XML file and applies them to the results.
	 * 
	 * @param result
	 *            results to filter
	 * @param fileName
	 *            XML file for filters
	 * @return filtered results
	 */

	private void applyFilters(String fileName) {
		FilterConfigReader fcr = new FilterConfigReader(fileName);
		while (fcr.hasNext()) {
			fcr.next();
			if (fcr.getTagsElement() != null)
				for (Result r : flatten())
					runFilter(fcr, r);
			else
				runFilter(fcr, root);
		}
	}

	/**
	 * Removes the old child from its parent's components and adds the new child in its place.
	 * 
	 * @param oldChild
	 *            old child
	 * @param newChild
	 *            new child to set the reference to
	 */

	private void fixParent(Result oldChild, Result newChild) {
		ResultComposite parent = getParent(oldChild);
		if (parent != null) {
			int childPosition = parent.getChildren().indexOf(oldChild);
			parent.getChildren().set(childPosition, newChild);
		}
	}

	/**
	 * Returns the tree as a flat list.
	 * 
	 * @return tree as list
	 */

	private ArrayList<Result> flatten() {
		ArrayList<Result> resultList = new ArrayList<Result>();
		flatten_rec(root, resultList);
		return resultList;
	}

	/**
	 * Respective recursive function.
	 */
	private void flatten_rec(Result result, ArrayList<Result> resultList) {
		resultList.add(result);
		for (Result r : result.getChildren())
			flatten_rec(r, resultList);
	}

	/**
	 * Get first method with the specified name; hopefully setters aren't overloaded.
	 * 
	 * @param clazz
	 *            class to get method from
	 * @param methodName
	 *            name of method to get
	 * @return the method or null if not found
	 */

	private Method getMethod(Class<? extends Filter> clazz, String methodName) {
		for (Method method : Arrays.asList(clazz.getDeclaredMethods()))
			if (method.getName().equals(methodName))
				return method;
		return null;
	}

	/**
	 * Returns the child's parent.
	 * 
	 * @param child
	 *            child to find parent for
	 * @return child's parent or null in case the child was the root
	 */

	private ResultComposite getParent(Result child) {
		ArrayList<Result> resultList = flatten();
		for (Result r : resultList)
			if (r instanceof ResultComposite)
				if (r.getChildren().contains(child))
					return (ResultComposite) r;
		return null;
	}

	/**
	 * Returns the class' setter for the specified field mostly does checks for validity.
	 * 
	 * @param clazz
	 *            class to get the setter from
	 * @param fieldName
	 *            name of the setter's field
	 * @return setter for the field
	 */

	private Method getSetter(Class<? extends Filter> clazz, String fieldName) {
		Method setter = getMethod(clazz, "set" + WordUtils.capitalize(fieldName));

		if (setter == null) // no setter found
			throw new RuntimeException("Filter \"" + clazz.getName() + "\" doesn't accept parameter " + fieldName);

		Type[] parameterTypes = setter.getGenericParameterTypes();
		if (parameterTypes.length != 1) // too many or no arguments
			throw new RuntimeException("Setter for parameter " + fieldName
					+ " found but with invalid number of arguments of " + parameterTypes.length);

		Type parameterType = parameterTypes[0];

		if (!isValidParameterType(parameterType)) // unsupported argument type
			throw new RuntimeException("Setter for parameter " + fieldName + " found but with invalid argument type "
					+ parameterType);

		// should have a setter with one argument of a supported type now
		return setter;
	}

	/**
	 * Checks if the type represented by the class is currently accepted.
	 * 
	 * @param type
	 * @return
	 */

	private static boolean isValidParameterType(Type type) {
		// list of supported types: primitives and String
		if (type.equals(Byte.TYPE) || type.equals(Short.TYPE) || type.equals(Integer.TYPE) || type.equals(Long.TYPE)
				|| type.equals(Float.TYPE) || type.equals(Double.TYPE) || type.equals(Boolean.TYPE)
				|| type.equals(Character.TYPE) || type.equals(String.class))
			return true;
		return false;
	}

	/**
	 * Parses a parameter according to its type.
	 * 
	 * @param parameter
	 *            parameter to parse
	 * @param type
	 *            its type
	 * @return parsed parameter
	 */

	private Object parseParameter(String parameter, Type type) {
		if (type.equals(String.class))
			return parameter;
		if (type.equals(Byte.TYPE))
			return Byte.parseByte(parameter);
		if (type.equals(Short.TYPE))
			return Short.parseShort(parameter);
		if (type.equals(Integer.TYPE))
			return Integer.parseInt(parameter);
		if (type.equals(Long.TYPE))
			return Long.parseLong(parameter);
		if (type.equals(Float.TYPE))
			return Float.parseFloat(parameter);
		if (type.equals(Double.TYPE))
			return Double.parseDouble(parameter);
		if (type.equals(Boolean.TYPE))
			return Boolean.parseBoolean(parameter);
		if (type.equals(Character.TYPE))
			if (parameter.length() == 1)
				return parameter.charAt(0);
			else
				throw new RuntimeException("Failed to parse parameter: " + parameter
						+ " as char because its length is not 1");

		throw new RuntimeException(
				"Inconsistent types between parseParameter and isValidParameter; should never happen");
	}

	/**
	 * Runs a single filter on a given result.
	 * 
	 * @param fcr
	 *            filter config reader with respective filter
	 * @param result
	 *            result to use the filter on
	 */

	private void runFilter(FilterConfigReader fcr, Result result) {
		try {
			Class<?> clazz = Class.forName(fcr.getClassName());

			if (TagValidator.matches(result, fcr.getTagsElement())) {
				Filter filter = (Filter) clazz.newInstance();
				filter.setProcessedResource(requestURI);
				filter.setModel(request.getModel());
				setParameters(filter, fcr);
				Result tempResult = result.use(filter);
				setMetaData(tempResult, fcr);
				fixParent(result, tempResult);
				if (result == root)
					root = tempResult;
			}

		} catch (Exception e) {
			if (!(e instanceof RuntimeException))
				throw new RuntimeException(e); // if e. g.
												// ClassNotFoundException pass
												// on as RuntimeException
			else if (AppConfig.isDebugMode())
				throw new RuntimeException(e); // to find the actual source of
												// the exception
			else if (e instanceof NullPointerException)
				throw new RuntimeException("FilterComposite can not be used with Table");
			else
				throw new RuntimeException(e.getMessage()); // to avoid
															// ..RuntimeExeption:
															// ..
															// RuntimeException:
		}
	}

	/**
	 * Call to signal error during request.
	 * 
	 * @param message
	 *            message to display
	 * @return returns true if no error had happened before
	 */
	private boolean setError(String message) {
		boolean oldState = errorOccurred;
		errorOccurred = true;
		if (!errorMessage.equals(""))
			errorMessage += "; ";
		errorMessage += message;
		return !oldState;
	}

	/**
	 * Sets the meta data (tags and captions) on a result.
	 * 
	 * @param result
	 *            result to set the meta data on
	 * @param fcr
	 *            filter config reader with the respective meta data
	 */

	private void setMetaData(Result result, FilterConfigReader fcr) {

		if (fcr.getCaption() != null) // set caption
			result.setCaption(fcr.getCaption());

		if (fcr.getAddTags().size() > 0) { // set tags if there are tags to set
			for (AddTagParameter addTagParameter : fcr.getAddTags()) {
				String tagName = addTagParameter.getTagName();
				for (int sonToAddTag : addTagParameter.getIndices()) {
					if (sonToAddTag < result.getChildren().size()) // set tag
																	// if son
																	// exists
						result.getChildren().get(sonToAddTag).addTag(tagName);
				}
			}
		}

	}

	/**
	 * Sets the parameters in the filter.
	 * 
	 * @param filter
	 *            filter to set fields in
	 * @param fcr
	 *            reader with the parameters
	 */

	private void setParameters(Filter filter, FilterConfigReader fcr) {
		try {
			ArrayList<NameValue> parameters = fcr.getParameters();
			for (NameValue parameter : parameters) {
				Class<? extends Filter> clazz = filter.getClass();

				Method setter = getSetter(clazz, parameter.getName());
				Type type = setter.getGenericParameterTypes()[0];
				Object parsedParameter = parseParameter(parameter.getVal(), type);
				setter.invoke(filter, parsedParameter);
			}
		} catch (Exception e) {
			throw new RuntimeException("Need to improve setter parsing: " + e);
		}
	}
}
