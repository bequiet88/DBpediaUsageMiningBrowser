package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import utils.Utils;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Class for fixes for RDF data.
 * 
 * Not used right now.
 * 
 */
public class RDFFix {

	public static void fixDarmstadt(Model model, String requestString) throws IOException {
		BufferedReader br = null;
		File f = new File("result.rdf");
		Writer writer = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");

		try {
			URL url = new URL(Utils.fixForData(requestString));
			URLConnection conn = url.openConnection();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				if (!inputLine.trim().startsWith("<p:")) {
					writer.write(inputLine);
					writer.write(System.getProperty("line.separator"));
				}
			}
		} catch (MalformedURLException mue) {
			throw new RuntimeException(mue);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		} finally {
			try {
				br.close();
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}
		}
		writer.flush();
		writer.close();

		InputStream IS = new FileInputStream("result.rdf");
		model.read(IS, null);
	}
	
	/**
	 * tries to fix queries so that a a url connection can be opened directly to a dbpedia resource
	 * 
	 * If the jena framework is used a dbpedia resource's rdf-file can be reached by model.read with
	 * http://dbpedia.org/resource/REQUESTEDRESOURCE. Otherwise you have to open a url connection to
	 * http://dbpedia.org/data/REQUESTEDRESOURCE.rdf .
	 * 
	 * @param query
	 *            String to fix
	 * @return fixed String
	 */

	public static String fixForData(String s) {
		if (s.startsWith(RESSTR)) {
			return s.replace(RESSTR, "http://dbpedia.org/data/").concat(".rdf");
		} else
			return "http://dbpedia.org/data/".concat(Utils.fixNatural(s)).concat(".rdf");
	}

}
