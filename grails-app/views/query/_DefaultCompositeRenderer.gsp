<!-- the default renderer for all results  -->
<!-- renders triples as text in three columns and indents clusters  -->
<%@page import="utils.Utils" %> 
<%@page import="result.Result" %> 
<%@page import="result.ResultComposite" %> 
<%@page import="result.Table" %> 


<g:set var="viewResultList"
	value="${Utils.flattenToViewResults(result)}" />
<g:set var="i" value="${0}" />

<g:while test="${i < viewResultList.size()}">
	<g:set var="viewResult" value="${viewResultList.get(i)}" />
	<g:if test="${viewResult.getClusterStart()}">

		<g:if test="${viewResult.getCaption() != null}">
			<hr class="captionTop">
			<p align="center">
				<b> ${viewResult.getCaption()}
				</b>
			</p>
			<hr class="captionBottom">
		</g:if>
		<div class="rendererCluster">
	</g:if>
	<g:else>
		<g:if test="${viewResult.getClusterEnd()}">
			</div>
		</g:if>
		<g:else>
			<g:if test="${viewResult.getCaption() != null}">
				<hr class="captionTop">
				<p align="center">
					<b> ${viewResult.getCaption()}
					</b>
				</p>
				<hr class="captionBottom">
			</g:if>
			<table>
				<tr>
					<th>Subject</th>
					<th>Predicate</th>
					<th>Object</th>
				</tr>
				<g:each in="${viewResult.getTable() }" status="j" var="result">
					<g:set var="sub" value="${Utils.utf8toUnicode(result.sub)}" />
					<g:set var="pred" value="${Utils.utf8toUnicode(result.pred)}" />
					<g:set var="obj" value="${Utils.utf8toUnicode(result.obj)}" />

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
			<g:if test="${viewResultList.size() > i + 1}">
				<g:if
					test="${!viewResultList.get(i + 1).getClusterEnd() && !viewResultList.get(i + 1).getClusterStart() }">
					<!-- set hr-tag if next ViewResult is not end not start start of a cluster -->
					<hr>
				</g:if>
			</g:if>
		</g:else>
	</g:else>
	<%i++%>
</g:while>
