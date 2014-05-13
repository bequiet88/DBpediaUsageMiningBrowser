<!--  basically the same as DefaultCompositeRenderer but only renders the object and predicate columns -->
<!--  targettype is composite -->
<%@page import="utils.Utils" %> 
<%@page import="result.Result" %> 
<%@page import="result.ResultComposite" %> 
<%@page import="result.Table" %> 
<%@page import="view.JavaObjPredRenderer" %> 

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
			
			${JavaObjPredRenderer.renderTable(result, token) }
			<g:if test="${viewResultList.size() > i + 1}">
				<g:if
					test="${!viewResultList.get(i + 1).getClusterEnd() && !viewResultList.get(i + 1).getClusterStart() }">
					<!-- set hr-tag if next ViewResult is not end and not start of a cluster -->
					<hr>
				</g:if>
			</g:if>
		</g:else>
	</g:else>
	<%i++%>
</g:while>
