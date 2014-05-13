package ldb

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutionException

import request.PreCheckRunner
import request.QueryHandler
import request.QueryRunner
import request.Request
import result.Result
import utils.AppConfig
import utils.Debug
import utils.Utils
import view.RendererEntry
import view.RendererReader




/**
 * Controller for queries.
 *
 */

class QueryController {

	def index() {
	}


	/**
	 * Prints the threadmap.
	 */
	public static void printMap(){
		println "threadMap entries are: "
		for(String key : threadMap.keySet())
			println key + " " + threadMap.get(key);
	}

	/**
	 * Cancels a user's query identified by its token.
	 */

	def cancelQueries = {
		if(params.token != null && params.token != ""){
			Thread threadToStop = threadMap.get(params.token)
			if(threadToStop != null){
				threadToStop.stop()
				println threadToStop.toString() + "stopped"
				threadMap.remove(params.token)
			}
		}
	}

	/**
	 * Cancels all currently running queries by setting the requestCancelled variable and if enabled stopping their threads.
	 */

	def cancelAllQueries = {
		println "trying to cancel all queries"
		requestCancelled = true;
		if(threads.size() > 0 && AppConfig.isCancelByStop()){
			for(Thread thread : threads)
				thread.stop()
			println "All threads stopped"
			threads.clear()
		}
	}

	//used to indicate errors to be returned asynchronously
	//empty message ("") equals success
	String queryMessage = "";

	//this is set to true if the cancel request button is pressed
	static boolean requestCancelled = false
	//gets reset after each query
	boolean requestCancelledLocal = false

	String token;

	//list of all internal threads for cancel all queries
	public static List<Thread> threads = new ArrayList<Thread>();
	//maps threads to users by tokens
	public static ConcurrentHashMap<String, Thread> threadMap = new ConcurrentHashMap<String, Thread>();
	//maps ready results in form of RendererEntry lists to users by tokens
	public static ConcurrentHashMap<String, Result> rendererMap = new ConcurrentHashMap<String, List<RendererEntry>>();


	/**
	 * Main function for delegating queries.
	 */
	def query = {

		try{

			//reset variables
			queryMessage = "";
			requestCancelled = false
			requestCancelledLocal = false

			//get token
			if(params.token == null || params.token == "" || params.querytext == null || params.querytext =="" )
				token = Utils.getNewToken();
			else token = params.token

			request.token = token

			//cancel previous queries by this user
			cancelQueries()


			//if nothing has been entered (first time the page is opened) we're done
			if(params.querytext == null)
				return

			//begin query
			println "---- query " + params.querytext + " begin ----";
			Debug.startTimer();

			Request uriRequest = new Request(params.querytext);
			Request naturalRequest = new Request(Utils.fixNatural(params.querytext));

			try{
				//first try to see if the request string will immediately resolve to an RDF resource
				if (Debug.printDelay("before precheck #1") & PreCheckRunner.runPreCheckInThread(uriRequest, token) & Debug.printDelay("after precheck #1"))
					runQueryDirectly(uriRequest)
				else
				//else assume it's a natural language query
				if(Debug.printDelay("before precheck #2") & PreCheckRunner.runPreCheckInThread(naturalRequest, token) & Debug.printDelay("after precheck #2"))
					runQueryDirectly(naturalRequest)
				else {
					queryMessage = "No RDF found for query \""  + params.querytext + "\""
				}
			}catch(ExecutionException e){
				//exception with ThreadDeath indicates a cancelled query
				if(e.message == "java.lang.ThreadDeath")
					queryMessage = "Query " + params.querytext + " cancelled"
				else {queryMessage = e.message{
						e.printStackTrace()
						println "stacktrace size: " + e.stackTrace.size()
						if(AppConfig.isDebugMode()){
							render e.toString()
							render e.message
							render e.stackTrace
							return
						}
					}
				}
			}

			//catch all exceptions and turn them into a message for the asynchronous response
		}catch(Exception e){
			e.printStackTrace()
			queryMessage = e.getMessage()
			if(AppConfig.isDebugMode()){
				/*
				 for(int i = 0; i < e.stackTrace.size(); i++)
				 println e.stackTrace[i];
				 */
				println "stacktrace size: " + e.stackTrace.size()

				render e.toString()
				render e.message
				render e.stackTrace
				return
			}
		}

		render queryMessage
	}

	/**
	 * Main function for running queries.
	 * @param uri URI to query
	 * @return void
	 */
	private void runQueryDirectly(Request currentRequest) {
		Debug.printDelay("beginRunQueryDirectly")
		//run query
		QueryHandler newQueryHandler
		newQueryHandler  = QueryRunner.runQueryInThread(currentRequest, token);


		//retrieve results
		Result result = newQueryHandler.getResult()
		if(newQueryHandler.errorOccurred)
			throw new RuntimeException(newQueryHandler.errorMessage)


		//get renderers
		String rendererfile = AppConfig.getRendererFile()
		if (result != null && result.size() != 0){
			RendererReader rendererReader = new RendererReader(rendererfile, result)
			rendererMap.put(token, rendererReader.getRendererEntries());
		}

		Debug.printDelay("endRunQueryDirectly")


		if(AppConfig.isSaveQuery())
			Serialization.saveQuery(Utils.unicodeToJenaString(uri), newQueryHandler.getResult());

	}

	/**
	 * Retrieves the results after an asynchronous query.
	 */

	def renderResults = {



		Debug.printDelay("Begin renderResults")

		request.querytext = params.querytext
		request.token = params.token



		if(params.querytext != null && params.querytext != ""){

			request.renderers = rendererMap.get(params.token);
			//if the result does not exist, run the query first
			if(request.renderers == null)
			{
				params.querytext = params.querytext
				params.token = params.token
				query()
				request.renderers = rendererMap.get(params.token)
			}

			rendererMap.remove(params.token);

			request.token2 = request.token

			Debug.printDelay("End renderResults")

			forward(action:"index")
		}

	}



	/**
	 * Runs a precheck to test if this URI represents RDF and returns the result to the client.
	 */
	def checkForRDF = {


		Request uriRequest = new Request(params.wholeQuery);
		if (PreCheckRunner.runPreCheckInThread(uriRequest, 3000))
			render ""
		else render params.wholeQuery

	}

}
