<!-- the default renderer for all results  -->
<!-- renders triples as text in three columns and indents clusters  -->
<!--  can have targettype composite and table but composite looks better -->
<%@page import="utils.Utils" %> 
<%@page import="view.JavaDefaultRenderer" %> 
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
			${JavaDefaultRenderer.renderTable(viewResult.getTable(), token) } 
			
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
