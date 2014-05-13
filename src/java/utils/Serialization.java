package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import result.Result;

/**
 * Class for conveniently saving and loading results to/from files.
 * 
 */
public class Serialization {
	private static final String BASEPATH = "";
	private static final String EXTENSION = ".res";

	/**
	 * Serializes the result to a file.
	 * 
	 * @param result
	 *            result to save
	 * @param query
	 *            query used to determine the filename
	 */

	public static void saveResult(Result result, String query) {
		try {
			if (result != null) {
				String filename = BASEPATH + query2filename(query);
				ObjectOutput out = new ObjectOutputStream(new FileOutputStream(filename));
				out.writeObject(result);
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Loads a result.
	 * 
	 * @param filename
	 *            name of file to load
	 * @return
	 */

	public static Result loadResult(String filename) {
		try {
			File file = new File(filename);
			ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
			Object result = oin.readObject();
			oin.close();
			return (Result) result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static Result loadResult(String name, boolean isQuery) {
		if (isQuery)
			return loadResult(query2filename(name));
		else
			return loadResult(name);
	}

	/**
	 * Verifies that the result was properly saved.
	 * 
	 * @param result
	 *            result to save
	 * @param query
	 *            query it was saved as
	 * @return
	 */

	public static boolean verify(Result result, String query) {
		Result compare = loadResult(query2filename(query));

		return result.equals(compare);
	}

	public static boolean verify(Result result, String query, boolean isQuery) {
		return true;
	}

	/**
	 * Converts a query to a filename.
	 * 
	 * Needs to remove characters that are not allowed in filenames
	 * 
	 * @param query
	 *            query to convert
	 * @return query as a valid filename
	 */

	private static String query2filename(String query) {
		String filename = Utils.unicodeToUtf8(query);
		filename += EXTENSION;
		return filename;
	}

	/**
	 * Saves a query for local mode.
	 */

	public static boolean saveQuery(String filename, Result result) {
		// make sure we only save unfiltered results

		if (AppConfig.getFilterFile().equals("FilterFileFolder/emptyFilters.xml")) {
			Serialization.saveResult(result, filename);
			return (Serialization.verify(result, filename));

		}

		else
			return false;
	}
}
