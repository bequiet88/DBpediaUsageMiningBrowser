package filters;

import java.util.Date;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class SPARQLEndpoint {
	private String endpointURL;
	
	private boolean verbose = false;
	private long criticalTime = 100;
	
	public SPARQLEndpoint(String endpointURL) {
		this.endpointURL = endpointURL;
	}
	
	public ResultSet runSelect(String q) {
		ResultSet RS = null;
		Date d0 = new Date();
		QueryEngineHTTP qexec = new QueryEngineHTTP(endpointURL, q);
		qexec.addParam("timeout", "120000");
		boolean OK = false;
		while(!OK) {
			try {
				RS =  qexec.execSelect();
				OK = true;
			} catch(Exception e) {
				if(verbose) {
					System.err.println(e.getMessage());
					System.err.println("SPARQL endpoint failed, retrying...");
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		long time = new Date().getTime() - d0.getTime();
		if(verbose && time>criticalTime) {
			System.out.println(q);
			System.out.println(time + " milliseconds");
		}
		return RS;
	}
	
	public boolean runAsk(String q) {
		boolean b = false;
		Date d0 = new Date();
		QueryEngineHTTP qexec = new QueryEngineHTTP(endpointURL, q);
		qexec.addParam("timeout", "120000");
		boolean OK = false;
		while(!OK) {
			try {
				b= qexec.execAsk();
				OK = true;
			} catch(Exception e) {
				if(verbose) {
					System.err.println(e.getMessage());
					System.err.println("SPARQL endpoint failed, retrying...");
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		long time = new Date().getTime() - d0.getTime();
		if(verbose && time>criticalTime) {
			System.out.println(q);
			System.out.println(time + " milliseconds");
		}
		return b;
	}
}
