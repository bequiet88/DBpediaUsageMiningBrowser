package view;

import java.util.List;

import ldb.RDFTriple;
import result.Table;
import utils.AppConfig;
import utils.Utils;

/**
 * ObjPredRenderer in Java
 * 
 */
public class JavaObjPredRenderer {

	public static String renderTable(Table table, String token) {
		return renderTable(table.getTriples(), token);
	}

	public static String renderTable(List<RDFTriple> triples, String token) {
		StringBuilder sb = new StringBuilder();

		if (AppConfig.isDebugMode())
			sb.append("Number of triples: " + triples.size() + "\n");

		// add table header
		sb.append("<table>\n<tr>\n<th>Predicate</th>\n<th>Object</th>\n</tr>\n");

		for (RDFTriple triple : triples) {

			// first cell begin
			sb.append("<tr><td>\n<a href=\"");

			// predicate column
			sb.append(Utils.utf8toUnicode(triple.getPred()));
			sb.append("\" onclick=\"javascript:return checkForRDF(this);\">\n");
			sb.append(Utils.utf8toUnicode(triple.getPred()));

			// first cell end, second cell begin
			sb.append("</a>\n</td>\n<td>\n");
			if (triple.getObj().startsWith("http://") || triple.getObj().startsWith("http://")) {
				sb.append("<a href=\"");
				// object column
				sb.append(triple.getObj());
				sb.append("\" onclick=\"javascript:return checkForRDF(this);\">\n");
				sb.append(Utils.utf8toUnicode(triple.getObj()));
				// second cell end
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
