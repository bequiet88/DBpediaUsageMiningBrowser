package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

/**
 * Isolated class for testing Jena by printing models.
 * 
 */
public class JenaTest {

	// shut up log4j
	static {
		LogManager.getRootLogger().setLevel(Level.OFF);
	}

	public static void main(String[] args) {

		printResource("http://dbpedia.org/resource/&");

	}

	public static void printResource(String URI) {
		FileManager fManager = FileManager.get();
		fManager.addLocatorURL();
		Model model = fManager.loadModel(URI);
		printModel(model);
	}

	public static void printModel(Model model) {
		String queryString = "SELECT ?x ?y ?z " + "WHERE {" + "   ?x ?y ?z . " + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		ResultSetFormatter.out(System.out, results, query);
		qe.close();
	}

}
