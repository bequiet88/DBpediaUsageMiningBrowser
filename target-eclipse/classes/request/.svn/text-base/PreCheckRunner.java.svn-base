package request;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ldb.QueryController;

/*
 * Class for running prechecks in a separate thread.
 */

public class PreCheckRunner implements Callable<Boolean> {

	private Request request;
	private String token;

	public PreCheckRunner(Request request, String token) {
		this.request = request;
		this.token = token;
	}

	@Override
	public Boolean call() throws Exception {
		QueryController.threads.add(Thread.currentThread());
		QueryController.threadMap.put(token, Thread.currentThread());

		boolean preCheckSucces = request.preCheck();

		QueryController.threads.remove(Thread.currentThread());
		QueryController.threadMap.remove(token);
		return preCheckSucces;
	}

	/**
	 * Creates a new thread for this request's precheck and runs it.
	 * 
	 * @param request
	 *            request for which to run precheck
	 * @param token
	 *            token of the request's user
	 * @return true iff precheck was successful
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */

	public static Boolean runPreCheckInThread(Request request, String token) throws ExecutionException,
			TimeoutException {
		try {
			return runPreCheckInThread(request, token, -1);
		} catch (TimeoutException e) {
			return true;
		}
	}

	/**
	 * Creates a new thread for this request's precheck and runs it.
	 * 
	 * @param request
	 *            request for which to run precheck
	 * @param timeout
	 *            time in ms after which to timeout
	 * @return true iff precheck was successful
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public static Boolean runPreCheckInThread(Request request, int timeout) throws ExecutionException, TimeoutException {
		try {
			return runPreCheckInThread(request, "precheck", timeout);
		} catch (TimeoutException e) {
			return true;
		}

	}

	/**
	 * Actual function to run everything.
	 * 
	 * @param request
	 * @param token
	 * @param timeout
	 * @return
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */

	private static Boolean runPreCheckInThread(Request request, String token, int timeout) throws ExecutionException,
			TimeoutException {
		final ExecutorService service;
		final Future<Boolean> task;
		Boolean wasSuccessful;

		service = Executors.newFixedThreadPool(1);
		task = service.submit(new PreCheckRunner(request, token));

		try {
			if (timeout < 0)
				wasSuccessful = task.get();
			else
				wasSuccessful = task.get(timeout, TimeUnit.MILLISECONDS);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}

		service.shutdownNow();

		return wasSuccessful;
	}

}
