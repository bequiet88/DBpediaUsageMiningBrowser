package request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ldb.RDFTriple;

import org.apache.commons.io.IOUtils;

import result.Result;
import result.Table;
import utils.AppConfig;
import utils.Debug;
import utils.Utils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Responsible for one request. Accepts a URI as a string, retrieves the data from the server and runs the Jena query
 * against it.
 * 
 */
public class Request {

	private final static String emptyRDF = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<rdf:RDF\n	xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n	xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" >\n</rdf:RDF>";
	private String contentType = "";
	private String requestURI;
	private Result result = null;
	private byte[] bufferedResponse;

	/**
	 * Standard constructor.
	 * 
	 * @param requestURI
	 *            the URI to query
	 */
	public Request(String requestURI) {
		this.requestURI = requestURI;

	}

	/**
	 * Queries the data from the Internet.
	 * 
	 * If precheck hasn't been run yet it will be now.
	 * 
	 * @param the
	 *            query
	 * @return results results of the query
	 * @throws IOException
	 */

	public void runQuery() throws IOException {
		if (bufferedResponse == null)
			if (!preCheck())
				throw new RuntimeException("precheck on " + requestURI + " unsuccessful");

		Debug.printDelay("begin runQuery()");
		List<RDFTriple> res = new ArrayList<RDFTriple>();

		String queryString = "SELECT ?sub ?pred ?obj WHERE { { ?sub ?pred ?obj } }";
		Query query = QueryFactory.create(queryString);
		Model model = getModel();

		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		while (results.hasNext()) {
			QuerySolution qs = results.next();
			res.add(new RDFTriple(qs.get("sub"), qs.get("pred"), qs.get("obj")));
		}
		qe.close();

		if (res.size() == 0)
			throw new RuntimeException(requestURI + " not found");

		Table table = new Table(res);
		Debug.printDelay("end runQuery()");
		table.setUnderlyingModel(model);
		this.result = table;
	}

	public String getRequestURI() {
		return requestURI;
	}

	/**
	 * Gets the result of this request.
	 * 
	 * @return the result or null if this request hasn't been run yet
	 */

	public Result getResult() {
		return result;
	}

	/**
	 * Gets a Jena model from the buffered data.
	 * 
	 * @return the Jena model
	 */

	public Model getModel() {
		Model model = ModelFactory.createDefaultModel();
		try {
			Debug.printDelay("before Jena model.read()");
			ByteArrayInputStream bis = new ByteArrayInputStream(bufferedResponse);

			if (contentType.startsWith("text/turtle"))
				model.read(bis, null, "TURTLE");
			else if (contentType.startsWith("text/n3"))
				model.read(bis, null, "N3");
			else
				model.read(bis, null);
			Debug.printDelay("after Jena model.read()");
			System.out.println(model.getNsPrefixMap());
		} catch (Exception e) {
			throw new RuntimeException("Most likely this URI does not return an RDF file: " + e.toString());
		}
		return model;
	}

	/**
	 * Checks if this request's URI returns valid RDF and while doing so downloads the data behind this URI.
	 * 
	 * @return true iff this URI represents RDF data
	 */

	public boolean preCheck() {
		if (AppConfig.isLocalMode())
			return true;

		Debug.printDelay("begin precheck");
		try {
			requestURI = Utils.unicodeToJenaString(requestURI);
			URL url = new URL(requestURI);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.addRequestProperty("Accept","text/turtle,text/n3,appcliation/rdf+xml;q=0.9,text/plain;q=0.8");

			con.connect();
			Debug.printDelay("after connect");

			if (con.getResponseCode() == 200) {
				Debug.printDelay("after getResponseCode");
				contentType = con.getContentType();
				Debug.printDelay("after getContentType");
				if (contentType.startsWith("application/rdf+xml") || contentType.startsWith("text/turtle")
						|| contentType.startsWith("text/n3") ||
						// freebase returns "text/plain"
						// TODO check with Heiko
						(requestURI.contains("freebase") && contentType.startsWith("text/plain"))) {

					// actually download the data
					InputStream is = con.getInputStream();
					bufferedResponse = IOUtils.toByteArray(is);
					Debug.printDelay("after downloading data");

					// test if this URI retuns an empty RDF file as dbpedia does on every non-existing page
					if (Utils.bytesToString(bufferedResponse, bufferedResponse.length).equals(emptyRDF))
						return false;

					con.disconnect();
					return true;
				}
			}
			con.disconnect();
			return false;
		} catch (IOException e) {
			return false;
		}
	}

}