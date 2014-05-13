package utils;

/**
 * Helper class for debugging.
 * 
 * Holds the global debugMode variable and allows for printing time stamps during a request.
 * 
 */
public class Debug {

	// prevent instantiation
	private Debug() {

	}

	private static long requestTimer;
	private static boolean timerRunning = false;

	/**
	 * Starts the timer.
	 */
	public static void startTimer() {
		timerRunning = true;
		requestTimer = System.nanoTime();
	}

	/**
	 * Stops the timer.
	 */
	public static void stopTimer() {
		timerRunning = false;
	}

	/**
	 * Prints the delay relative to the last startTimer-call.
	 * 
	 * @param text
	 *            Text to print along the time stamp, i.e. "begin someFunction()"
	 * 
	 * @return Always true so it can be used in if clauses
	 */
	public static boolean printDelay(String text) {
		if (AppConfig.isDisplayDelays() && timerRunning) {
			long delay = System.nanoTime() - requestTimer;
			System.out.println(text + " delay is: \t" + (delay / 1000000l) + "\t ms");
		}

		return true;
	}

	/**
	 * Prints the delay relative to the last startTimer-call.
	 * 
	 * @param text
	 *            Text to print along the time stamp, i.e. "begin someFunction()"
	 */

	public static void printDelayV(String text) {
		printDelay(text);
	}

}
