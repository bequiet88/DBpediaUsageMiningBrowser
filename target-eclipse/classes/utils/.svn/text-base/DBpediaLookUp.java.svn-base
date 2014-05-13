package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Only one static method for using the DBpedia lookup service.
 * 
 */
public class DBpediaLookUp {

	public static ArrayList<String> lookUp(String searchString) {
		ArrayList<String> suggestions = new ArrayList<String>();

		// make sure the string is properly utf8 encoded
		searchString = Utils.utf8toUnicode(searchString);
		searchString = Utils.unicodeToJenaString(searchString);

		BufferedReader br = null;
		String suggestion;
		try {
			URL url = new URL(
					"http://lookup.dbpedia.org/api/search.asmx/PrefixSearch?QueryClass=&MaxHits=5&QueryString="
							+ searchString);
			URLConnection conn = url.openConnection();
			if (AppConfig.isLookUpTimeOut()) {
				conn.setConnectTimeout(1000);
				conn.setReadTimeout(3000);
			}
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				if (inputLine.trim().startsWith("<Result>")) {
					br.readLine();
					suggestion = br.readLine().trim().replace("<URI>", "").replace("</URI>", "");
					suggestions.add(suggestion);
				}
			}
			br.close();
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("DBPedia lookup service is unavailable: " + ioe.getMessage());
		}

		return suggestions;
	}

}
