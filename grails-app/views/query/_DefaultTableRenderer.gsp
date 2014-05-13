<!--  same as DefaultCompositeRenderer but with targettype table and hence more concise -->
<%@page import="utils.Utils" %> 
<%@page import="result.Result" %> 
<%@page import="result.ResultComposite" %> 
<%@page import="result.Table" %> 


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
<!-- insert columns -->
		<tr>
			<td><g:if test="${sub.startsWith("http://")}">
						
				<a href="${sub}" onclick="javascript:return checkForRDF(this);">${sub}</a>
			</g:if> <g:else>
				${sub}
			</g:else></td>
		<td><g:if test="${pred.startsWith("http://")}">
						
				<a href="${pred}" onclick="javascript:return checkForRDF(this);">${pred}</a>
			</g:if> <g:else>
				${pred }
			</g:else></td>
		<td><g:if test="${obj.startsWith("http://")}">

				<a href="${obj}" onclick="javascript:return checkForRDF(this);">${obj}</a>
			</g:if> <g:else>
				${obj }
			</g:else></td>
		</tr>
	</g:each>
</table>
