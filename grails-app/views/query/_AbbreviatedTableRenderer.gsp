<!--  same as DefaultCompositeRenderer but with targettype table and hence more concise -->
<%@page import="utils.Utils" %> 
<%@page import="result.Result" %> 
<%@page import="result.ResultComposite" %> 
<%@page import="result.Table" %> 
<%@page import="utils.NamespaceAbbreviation" %>

<!-- open table -->
<table border="1">
	<tr>
		<th>Subject</th>
		<th>Predicate</th>
		<th>Object</th>
	</tr>
	<!--  set local variables - one for each column -->
	<g:each in="${result.getTriples()}" var="triple">
		<g:set var="sub" value="${Utils.utf8toUnicode(triple.sub)}" />
		<g:set var="pred" value="${Utils.utf8toUnicode(triple.pred)}" />
		<g:set var="obj" value="${Utils.utf8toUnicode(triple.obj)}" />
		<g:set var="subtext" value="${NamespaceAbbreviation.getAbbreviatedString(sub,result)}"/> 
		<g:set var="predtext" value="${NamespaceAbbreviation.getAbbreviatedString(pred,result)}"/> 
		<g:set var="objtext" value="${NamespaceAbbreviation.getAbbreviatedString(obj,result)}"/> 
<!-- insert columns -->
		<tr>
			<td><g:if test="${sub.startsWith("http://")}">
						
				<a href="${sub}" onclick="javascript:return checkForRDF(this);">${subtext}</a>
			</g:if> <g:else>
				${subtext}
			</g:else></td>
		<td><g:if test="${pred.startsWith("http://")}">
						
				<a href="${pred}" onclick="javascript:return checkForRDF(this);">${predtext}</a>
			</g:if> <g:else>
				${predtext}
			</g:else></td>
		<td><g:if test="${obj.startsWith("http://")}">

				<a href="${obj}" onclick="javascript:return checkForRDF(this);">${objtext}</a>
			</g:if> <g:else>
				${objtext}
			</g:else></td>
		</tr>
	</g:each>
</table>
