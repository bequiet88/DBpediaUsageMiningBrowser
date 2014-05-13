<!--  targettype is table -->
<%@page import="utils.Utils" %> 
<%@page import="result.Table" %>
<%@page import="view.ColorScale" %>

<g:if test="${result instanceof Table}">
	<table>
		<tr>
			<th>Subject</th>
			<th>Predicate</th>
			<th>Object</th>
			<th style="width: 80px">Credibility</th>
		</tr>
		<g:each in="${result.getTriples()}" status="j" var="currentResult">
			<g:set var="sub" value="${Utils.utf8toUnicode(currentResult.sub)}" />
			<g:set var="pred" value="${Utils.utf8toUnicode(currentResult.pred)}" />
			<g:set var="obj" value="${Utils.utf8toUnicode(currentResult.obj)}" />
			<g:set var="credibility" value="${Integer.parseInt(currentResult.getMetadata("credibility"))}" />
			<g:set var="credibilityColor" value="${ColorScale.makeColor(credibility)}"/>
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
				<td style="background-color: ${credibilityColor}; width: 80px;">
					${credibility}%
				</td>
			</tr>
		</g:each>
	</table>
</g:if>

