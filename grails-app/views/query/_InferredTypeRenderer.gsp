<!--  simple renderer for direct types, simply provides a list of types. Adapted from _DefaultTableRender -->
<%@page import="utils.Utils" %> 
<%@page import="result.Result" %> 
<%@page import="result.ResultComposite" %> 
<%@page import="result.Table" %> 
<%@page import="view.ColorScale" %>
<%@page import="utils.NamespaceAbbreviation" %>


<!-- open table -->
<table border="1">
	<!--  set local variables - one for each column -->
	<g:each in="${result.getTriples()}" var="triple">
		<g:set var="type" value="${Utils.utf8toUnicode(triple.obj)}" />
		<g:set var="score" value="${(int) Float.parseFloat(triple.getMetadata("score"))*100}" />
		<g:set var="scorecolor" value="${ColorScale.makeGreyscale(score)}" />
		<g:set var="displayname" value="${NamespaceAbbreviation.getAbbreviatedString(type,result)}" />
			<!-- insert columns -->
		<tr>
		<td>
				<g:if test="${type.startsWith("http://")}">
								<a href="${type}" onclick="javascript:return checkForRDF(this);" style="color:${scorecolor}">${displayname}</a>
							</g:if> <g:else>
								<span style="color:${scorecolor}">${displayname}</span>
							</g:else> <span style="color:${scorecolor}">(${score}%)</span>
		</td>
		</tr>
	</g:each>
</table>