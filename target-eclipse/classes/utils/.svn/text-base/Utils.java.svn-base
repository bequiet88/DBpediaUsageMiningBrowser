package utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import ldb.RDFTriple;

import org.apache.commons.lang.WordUtils;
import org.codehaus.groovy.grails.web.context.ServletContextHolder;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import result.Result;
import result.Table;
import view.ViewResult;

/**
 * Some static utility functions mainly for the view for now.
 * 
 */

public class Utils {
	private final static String RESSTR = "http://dbpedia.org/resource/";

	/**
	 * prevent instantiation
	 */
	private Utils() {
	}

	/**
	 * @param result
	 *            results to flatten
	 * @return result tree as list ViewResult
	 */
	public static ArrayList<ViewResult> flattenToViewResults(Result result) {
		ArrayList<ViewResult> viewResultList = new ArrayList<ViewResult>();
		if (result instanceof Table) {
			// viewResultList.add(new ViewResult(true, false, null, null));
			viewResultList.add(new ViewResult(false, false, result.getCaption(), (Table) result));
			// viewResultList.add(new ViewResult(false, true, null, null));
			return viewResultList;
		}
		flattenToViewResults_rec(result, viewResultList);
		return viewResultList;
	}

	private static ArrayList<ViewResult> flattenToViewResults_rec(Result result, ArrayList<ViewResult> viewResultList) {
		if (result instanceof Table)
			viewResultList.add(new ViewResult(false, false, result.getCaption(), (Table) result));
		else {
			viewResultList.add(new ViewResult(true, false, result.getCaption(), null));
			for (Result element : result.getChildren())
				flattenToViewResults_rec(element, viewResultList);
			viewResultList.add(new ViewResult(false, true, null, null));
		}
		return viewResultList;

	}

	/**
	 * Converts a Unicode string to a Jena compatible string.
	 * 
	 * @param url
	 *            URL to convert
	 * @return converted URL
	 */

	public static String unicodeToJenaString(String url) {
		// decode first to make sure we don't double encode
		url = utf8toUnicode(url);
		return replaceASCII(unicodeToUtf8(url));

	}

	public static String unicodeToUtf8(String s) {
		try {
			// make sure we don't double encode characters
			String decodeFirst = URLDecoder.decode(s, "UTF-8");
			String utf8 = URLEncoder.encode(decodeFirst, "UTF-8");
			// need to re-substitute ASCII special characters

			return utf8;

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("not gonna happen");
		}
	}

	private static String replaceASCII(String s) {
		s = s.replace("%21", "!");
		s = s.replace("%22", "\"");
		s = s.replace("%23", "#");
		s = s.replace("%24", "$");
		// s = s.replace("%25", "%"); % should not be replaced
		s = s.replace("%26", "&");
		s = s.replace("%27", "'");
		s = s.replace("%28", "(");
		s = s.replace("%29", ")");
		s = s.replace("%2A", "*");
		s = s.replace("%2B", "+");
		s = s.replace("%2C", ",");
		s = s.replace("%2D", "-");
		s = s.replace("%2E", ".");
		s = s.replace("%2F", "/");

		s = s.replace("%3A", ":");
		s = s.replace("%3B", ";");
		s = s.replace("%3C", "<");
		s = s.replace("%3D", "=");
		s = s.replace("%3E", ">");
		s = s.replace("%3F", "?");
		s = s.replace("%40", "@");

		s = s.replace("%5B", "[");
		s = s.replace("%5C", "\\");
		s = s.replace("%%D", "]");
		s = s.replace("%5E", "^");
		s = s.replace("%5F", "_");
		s = s.replace("%60", "`");

		s = s.replace("%7B", "{");
		s = s.replace("%7C", "|");
		s = s.replace("%7D", "}");
		s = s.replace("%7E", "~");

		return s;
	}

	/**
	 * Converts an UTF-8 string to Unicode.
	 * 
	 * @param url
	 *            URL to convert
	 * @return converted URL
	 */

	public static String utf8toUnicode(String url) {
		try {
			return URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("not gonna happen");
		} catch (IllegalArgumentException e) {
			return url;
		}
	}

	/**
	 * Tries to fix queries so that natural language strings will be accepted too.
	 * 
	 * @param query
	 *            string to fix
	 * @return fixed String
	 */

	public static String fixNatural(String query) {
		if (query.startsWith("http://")) {
			return query;
		} else
			return RESSTR + WordUtils.capitalize(query).replace(" ", "_");
	}

	/**
	 * Creates a copy of the table's triples.
	 * 
	 * @param table
	 *            table to copy
	 * @return copy of the table's triples
	 */

	public static List<RDFTriple> cloneTriples(Table table) {
		ArrayList<RDFTriple> list = new ArrayList<RDFTriple>(table.getTriples().size());
		for (RDFTriple triple : table.getTriples())
			list.add(triple);
		return list;
	}

	/**
	 * Checks if DBpedia is up.
	 * 
	 * @return true if DBpedia is available else false
	 */

	public static boolean checkDBpedia() {
		return checkWebsite("http://dbpedia.org/page/Darmstadt");
	}

	/**
	 * Checks if a web site is available.
	 * 
	 * @param urlString
	 *            URL of the web site
	 * @return true iff the web site responds with 200
	 */

	public static boolean checkWebsite(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setRequestMethod("HEAD");
			huc.connect();
			int responseCode = huc.getResponseCode();
			huc.disconnect();
			return responseCode == 200;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Searches for a triple in the table with the specified subject and predicate.
	 * 
	 * @param table
	 *            table to search
	 * @param sub
	 *            subject to look for
	 * @param pred
	 *            predicate to look for
	 * @return the (first) fitting triple or null if not found
	 */

	public static RDFTriple getBySubPred(Table table, String sub, String pred) {
		for (RDFTriple triple : table.getTriples())
			if (triple.getSub().equals(sub) && triple.getPred().equals(pred))
				return triple;
		return null;
	}

	/**
	 * Reader for simple XML files. Reads property as an attribute of the root element.
	 * 
	 * @param filename
	 *            name of the XML file
	 * @param property
	 *            name of the attribute to read
	 * @return the attribute's value
	 */

	public static String readXML(String filename, String property) {
		URL url = getURL(filename);
		try {
			return new SAXBuilder().build(url).getRootElement().getAttribute(property).getValue();
		} catch (JDOMException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts an array of ASCII bytes to a Java string.
	 * 
	 * @param bytes
	 *            bytes to convert
	 * @param length
	 *            how many bytes to convert
	 * @return the resulting string
	 */

	public static String bytesToString(byte[] bytes, int length) {
		StringBuilder sb = new StringBuilder(bytes.length);
		for (int i = 0; i < length; i++)
			sb.append((char) bytes[i]);

		return sb.toString();
	}

	/**
	 * Gets a file by its name from config folder.
	 * 
	 * @param filename
	 *            name
	 * @return the URL of the file if it exists
	 */

	public static URL getURL(String filename) {

		// try to get file from springsource project directory
		File fileFromURI = new File("web-app/Config/" + filename);
		if (fileFromURI.exists())
			try {
				return fileFromURI.toURI().toURL();
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}

		// try to get file on Tomcat
		String contextPath = ServletContextHolder.getServletContext().getContextPath();
		File fileOnTomcat = new File("webapps/" + contextPath + "/Config/" + filename);
		if (fileOnTomcat.exists())
			try {
				return fileOnTomcat.toURI().toURL();
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}

		throw new RuntimeException("getFile(" + filename + ") failed");

	}

	/**
	 * Gets a file by its name from config folder.
	 * 
	 * @param filename
	 *            name of the file
	 * @return the URI of the file if it exists
	 */

	public static URI getURI(String filename) {
		try {
			return getURL(filename).toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets a file by its name.
	 * 
	 * @param filename
	 *            name of the file
	 * @return the file if it exists
	 */

	public static File getFile(String filename) {
		URL url = getURL(filename);
		File file;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			file = new File(url.getPath());
		}
		if (file.exists())
			return file;
		else
			throw new RuntimeException("File " + file.getAbsolutePath() + " not found");
	}

	/**
	 * Generates a new random token.
	 * 
	 * @return the token as a string
	 */

	public static String getNewToken() {
		return new BigInteger(130, new SecureRandom()).toString(32);
	}

}
