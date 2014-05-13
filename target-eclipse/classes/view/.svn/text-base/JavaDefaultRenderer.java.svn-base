package view;

import java.util.List;

import ldb.RDFTriple;
import result.Table;
import utils.AppConfig;
import utils.Utils;

/**
 * Default Table Renderer in Java.
 * 
 */
public class JavaDefaultRenderer {

	public static String renderTable(Table table, String token) {
		return renderTable(table.getTriples(), token);
	}

	public static String renderTable(List<RDFTriple> triples, String token) {
		StringBuilder sb = new StringBuilder();

		if (AppConfig.isDebugMode())
			sb.append("Number of triples: " + triples.size() + "\n");

		// add table header
		sb.append("<table>\n<tr>\n<th>Subject</th>\n<th>Predicate</th>\n<th>Object</th>\n</tr>");

		for (RDFTriple triple : triples) {
			// subject column
			sb.append("<tr>\n<td>\n<a href=\"");
			sb.append(Utils.utf8toUnicode(triple.getSub()));
			sb.append("\" onclick=\"javascript:return checkForRDF(this);\">\n");
			sb.append(Utils.utf8toUnicode(triple.getSub()));

			// first cell end, second cell begin
			sb.append("</a>\n</td>\n<td>\n<a href=\"");

			// predicate column
			sb.append(Utils.utf8toUnicode(triple.getPred()));
			sb.append("\" onclick=\"javascript:return checkForRDF(this);\">\n");
			sb.append(Utils.utf8toUnicode(triple.getPred()));

			// second cell end, third cell begin
			sb.append("</a>\n</td>\n<td>\n");
			if (triple.getObj().startsWith("http://") || triple.getObj().startsWith("http://")) {
				sb.append("<a href=\"");
				// object column
				sb.append(triple.getObj());
				sb.append("\" onclick=\"javascript:return checkForRDF(this);\">\n");
				sb.append(Utils.utf8toUnicode(triple.getObj()));
				// third cell end
				sb.append("\n</a>\n");
			} else
				sb.append(Utils.utf8toUnicode(triple.getObj()));
			sb.append("</td>\n</tr>");
		}

		// add table end
		sb.append("\n</table>");

		return sb.toString();
	}

}
