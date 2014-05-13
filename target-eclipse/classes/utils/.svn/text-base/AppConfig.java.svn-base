package utils;

import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * Makes the XML configuration available as getters and allows for overriding it.
 * 
 */
public class AppConfig {

	private static String xmlFile = "appconfig.xml";

	private static Boolean saveQuery = null;
	private static String rendererFile = null;
	private static String filterFile = null;
	private static Boolean debugMode = null;
	private static Boolean localMode = null;
	private static Boolean lookupEnabled = null;
	private static Boolean cancelByStop = null;
	private static Boolean enableCancelButton = null;
	private static Boolean displayDelays = null;
	private static Boolean enableCancelAllButton = null;
	private static Boolean localModeWithRDF = null;
	private static Boolean lookUpTimeOut = null;

	private AppConfig() {

	}

	/**
	 * Resets all overridden properties.
	 */

	public static void resetOverrides() {
		saveQuery = null;
		rendererFile = null;
		filterFile = null;
		debugMode = null;
		localMode = null;
		lookupEnabled = null;
		cancelByStop = null;
		enableCancelButton = null;
		displayDelays = null;
		enableCancelAllButton = null;
		localModeWithRDF = null;
		lookUpTimeOut = null;

	}

	/**
	 * Sets the XML file that properties are read from.
	 * 
	 * @param xmlFile
	 *            name of the new XML file
	 */
	public static void setXmlFile(String xmlFile) {
		AppConfig.xmlFile = xmlFile;
	}

	/**
	 * Overrides this property in memory.
	 * 
	 * Doesn't write anything to XML
	 * 
	 * Call resetOverrides() to reset everything.
	 * 
	 */

	public static void overrideEnableCancelAllButton(Boolean enableCancelAllButton) {
		AppConfig.enableCancelAllButton = enableCancelAllButton;
	}

	/**
	 * Overrides this property in memory.
	 * 
	 * Doesn't write anything to XML
	 * 
	 * Call resetOverrides() to reset everything.
	 * 
	 */

	public static void overrideDisplayDelays(Boolean displayDelays) {
		AppConfig.displayDelays = displayDelays;
	}

	/**
	 * Overrides this property in memory.
	 * 
	 * Doesn't write anything to XML
	 * 
	 * Call resetOverrides() to reset everything.
	 * 
	 */

	public static void overrideCancelByStop(Boolean cancelByStop) {
		AppConfig.cancelByStop = cancelByStop;
	}

	/**
	 * Overrides this property in memory.
	 * 
	 * Doesn't write anything to XML
	 * 
	 * Call resetOverrides() to reset everything.
	 * 
	 */

	public static void overrideEnableCancelButton(Boolean enableCancelButton) {
		AppConfig.enableCancelButton = enableCancelButton;
	}

	/**
	 * Overrides this property in memory.
	 * 
	 * Doesn't write anything to XML
	 * 
	 * Call resetOverrides() to reset everything.
	 * 
	 */

	public static void overrideSaveQuery(Boolean saveQuery) {
		AppConfig.saveQuery = saveQuery;
	}

	/**
	 * Overrides this property in memory.
	 * 
	 * Doesn't write anything to XML
	 * 
	 * Call resetOverrides() to reset everything.
	 * 
	 */

	public static void overrideRendererFile(String rendererFile) {
		AppConfig.rendererFile = rendererFile;
	}

	/**
	 * Overrides this property in memory.
	 * 
	 * Doesn't write anything to XML
	 * 
	 * Call resetOverrides() to reset everything.
	 * 
	 */

	public static void overrideFilterFile(String filterFile) {
		AppConfig.filterFile = filterFile;
	}

	/**
	 * Overrides this property in memory.
	 * 
	 * Doesn't write anything to XML
	 * 
	 * Call resetOverrides() to reset everything.
	 * 
	 */

	public static void overrideLocalMode(Boolean localMode) {
		AppConfig.localMode = localMode;
	}

	/**
	 * Overrides this property in memory.
	 * 
	 * Doesn't write anything to XML
	 * 
	 * Call resetOverrides() to reset everything.
	 * 
	 */

	public static void overrideDebugMode(Boolean debugMode) {
		AppConfig.debugMode = debugMode;
	}

	/**
	 * Overrides this property in memory.
	 * 
	 * Doesn't write anything to XML
	 * 
	 * Call resetOverrides() to reset everything.
	 * 
	 */

	public static void overrideLookupEnabled(Boolean lookupEnabled) {
		AppConfig.lookupEnabled = lookupEnabled;
	}

	/**
	 * Overrides this property in memory.
	 * 
	 * Doesn't write anything to XML
	 * 
	 * Call resetOverrides() to reset everything.
	 * 
	 */

	public static void overrideLocalModeWithRDF(Boolean localModeWithRDF) {
		AppConfig.localModeWithRDF = localModeWithRDF;
	}

	/**
	 * Overrides this property in memory.
	 * 
	 * Doesn't write anything to XML
	 * 
	 * Call resetOverrides() to reset everything.
	 * 
	 */

	public static void overrideLookUpTimeOut(Boolean lookUpTimeOut) {
		AppConfig.lookUpTimeOut = lookUpTimeOut;
	}

	public static boolean isSaveQuery() {
		return saveQuery != null ? saveQuery : Boolean.parseBoolean(read(xmlFile, "saveQuery"));
	}

	public static String getRendererFile() {
		return rendererFile != null ? rendererFile : read(xmlFile, "rendererFile");
	}

	public static String getFilterFile() {
		return filterFile != null ? filterFile : read(xmlFile, "filterFile");
	}

	public static boolean isDebugMode() {
		return debugMode != null ? debugMode : Boolean.parseBoolean(read(xmlFile, "debugMode"));
	}

	public static boolean isLocalMode() {
		return localMode != null ? localMode : Boolean.parseBoolean(read(xmlFile, "localMode"));
	}

	public static boolean isLookupEnabled() {
		return lookupEnabled != null ? lookupEnabled : Boolean.parseBoolean(read(xmlFile, "lookupEnabled"));
	}

	public static boolean isCancelByStop() {
		return cancelByStop != null ? cancelByStop : Boolean.parseBoolean(read(xmlFile, "cancelByStop"));
	}

	public static boolean isEnableCancelButton() {
		return enableCancelButton != null ? enableCancelButton : Boolean.parseBoolean(read(xmlFile,
				"enableCancelButton"));
	}

	public static boolean isDisplayDelays() {
		return displayDelays != null ? displayDelays : Boolean.parseBoolean(read(xmlFile, "displayDelays"));
	}

	public static boolean isEnableCancelAllButton() {
		return enableCancelAllButton != null ? enableCancelAllButton : Boolean.parseBoolean(read(xmlFile,
				"enableCancelAllButton"));
	}

	public static boolean isLocalModeWithRDF() {
		return localModeWithRDF != null ? localModeWithRDF : Boolean.parseBoolean(read(xmlFile, "localModeWithRDF"));
	}

	public static boolean isLookUpTimeOut() {
		return lookUpTimeOut != null ? lookUpTimeOut : Boolean.parseBoolean(read(xmlFile, "lookUpTimeOut"));
	}

	/**
	 * Reads a property of an XML file with format <config><property option="value">...</config>
	 * 
	 * @param filename
	 *            name of the XML file
	 * @param property
	 *            property to read
	 * @return its value
	 */

	private static String read(String filename, String property) {
		try {
			Element root = new SAXBuilder().build(Utils.getURL(xmlFile)).getRootElement();
			return root.getChild(property).getAttribute("option").getValue();
		} catch (JDOMException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
