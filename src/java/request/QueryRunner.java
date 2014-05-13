package request;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ldb.QueryController;

/*
 * Class for running queries in a separate thread.
 */

public class QueryRunner implements Callable<QueryHandler> {

	/**
	 * The Request handled by this runner.
	 */
	private Request request;
	/**
	 * The token of this request's user.
	 */
	private String token;

	/**
	 * Create a new Query runner.
	 * 
	 * @param request
	 *            Request to handle
	 * @param token
	 *            token of this request's user
	 */
	public QueryRunner(Request request, String token) {
		this.request = request;
		this.token = token;
	}

	@Override
	public QueryHandler call() throws Exception {
		// store this thread
		QueryController.threads.add(Thread.currentThread());
		QueryController.threadMap.put(token, Thread.currentThread());

		// runs the query
		QueryHandler newQueryHandler = new QueryHandler(request);
		newQueryHandler.run();

		// query done -> remove thread
		QueryController.threads.remove(Thread.currentThread());
		QueryController.threadMap.remove(token);
		return newQueryHandler;
	}

	/**
	 * Creates a new thread for this request and handles it.
	 * 
	 * @param request
	 *            request to run
	 * @return the resulting QueryHandler
	 * @throws ExecutionException
	 *             if query was cancelled
	 */

	public static QueryHandler runQueryInThread(Request request, String token) throws ExecutionException {

		final ExecutorService service;
		final Future<QueryHandler> task;

		QueryHandler queryHandler = null;

		service = Executors.newFixedThreadPool(1);
		// run query
		task = service.submit(new QueryRunner(request, token));

		// get result
		try {
			queryHandler = task.get();
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}

		// done
		service.shutdownNow();
		return queryHandler;
	}

}
