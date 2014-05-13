package utils;

import java.io.FileWriter;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Simple class for writing a custom renderer file to config.xml and restoring the original one afterwards.
 * 
 * Nothing is synchronized so don't try to use this in parallel.
 * 
 */
public class ConfigHelper {

	private static String rendererFileBackup = null, filterFileBackup = null;

	/**
	 * Writes the renderer file and backs up the original data.
	 * 
	 * @param rendererFile
	 *            renderer file to write
	 */

	public static void setConfig(String filterFile, String rendererFile) {
		try {
			rendererFileBackup = AppConfig.getRendererFile();
			filterFileBackup = AppConfig.getFilterFile();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try {
			Document doc = new SAXBuilder().build(Utils.getURL("appconfig.xml"));
			doc.getRootElement().getChild("rendererFile").setAttribute("option", rendererFile);
			doc.getRootElement().getChild("filterFile").setAttribute("option", filterFile);
			XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter(Utils.getFile("appconfig.xml")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Restores the config.xml (if the original content was backed up by setConfig).
	 */

	public static void restoreConfig() {
		if (rendererFileBackup != null && filterFileBackup != null)
			try {
				Document doc = new SAXBuilder().build(Utils.getURL("appconfig.xml"));
				doc.getRootElement().getChild("rendererFile").setAttribute("option", rendererFileBackup);
				doc.getRootElement().getChild("filterFile").setAttribute("option", filterFileBackup);
				XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
				xmlOutput.output(doc, new FileWriter(Utils.getFile("appconfig.xml")));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

	}

}
