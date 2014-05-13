<!-- main page that contains the input field and runs the renderers -->
<%@page import="view.RendererEntry"%>
<%@page import="utils.Debug"%>
<%@page import="utils.AppConfig"%>
<%@page import="utils.Utils" %>

<html>

<g:if test="${request.token2 == null}">
<g:set var="token" value="${Utils.getNewToken()}" />
</g:if>
<g:else>
<g:set var="token" value="${request.token2}" />
</g:else>
<head>
<g:javascript library="jquery"/>

<meta name="layout" content="main" />

<title>MoB4LOD</title>

<resource:autoComplete skin="test" />

<script type="text/javascript">

var queryIsCancelled = false;

//cancels current user's query
function cancelQueries(token){
	queryIsCancelled = true;
	var http = new XMLHttpRequest();
	var params = "token=" + token;
	http.open("POST", "cancelQueries", true);

	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.setRequestHeader("Content-length", params.length);
	http.setRequestHeader("Connection", "close");
	
	http.send(params);
	revertShowLoading()
}

//cancels all running queries
function cancelAllQueries(){
	queryIsCancelled = true;
	var http = new XMLHttpRequest();
	http.open("POST", "cancelAllQueries", true);
	http.send(null);
	revertShowLoading()
}

//to be called on click by elements that would run a new query
//if element.href doesn't represent RDF it will be replaced by
//a link to the website
//call as checkForRDF(this)
//returns false if href represents RDF to stop the browser from opening the link
function checkForRDF(element){

	showLoading()
	
	var isRDF;
	
	//if target is blank this URL was already tested
	if (element.target != "_blank") {
		
	jQuery.ajax({
		type:'POST',
		url:'${request.contextPath}/query/checkForRDF?wholeQuery=' + element.href,
		async: false,
		success:function(r){
			if(r == ''){
				isRDF = true;
			}
				else {
				element.target = "_blank";
				element.href = r;
				isRDF = false;
				}
			},
		error:function(e){}
		});
	} else {
		isRDF = false;
	}

	if (isRDF) {
		asyncQuery(element.href);
		}
	else {
		revertShowLoading();
		}

	return !(isRDF);
}


function isFirefox(){
	return navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
}

//show querySubmit again; if an url does not represent rdf, the main page is not reloaded hence
//loading gif has to disappear
function revertShowLoading() {
	document.getElementById("loading").className = "loading-invisible";
	document.querySubmit.style.display = "block";
}

//show loading gif (also prevents second querySubmit input)
function showLoading() {
	var totalHeight;
	
	totalHeight = document.querySubmit.offsetHeight;
	
	document.getElementById("loading").style.height=  totalHeight + 'px';
    document.querySubmit.style.display = "none";
  
    document.getElementById("loading").className = "loading-visible";
    
 }


//GET the results for this token from the server
function getResults(token, url){
	renderResultsUrl = '${request.contextPath}';
	renderResultsUrl += '/query/renderResults?token=';
	renderResultsUrl += token;
	renderResultsUrl += '&querytext='
	renderResultsUrl += url;
	window.location = renderResultsUrl;
	
	return false;
	}

//runs the query asynchronously
//the results will be stored under the token corresponding to this request
function asyncQuery(query){

	queryIsCancelled = false;
	if (!(document.querySubmit.style.display == "none"))
		showLoading();
	
	var tokenLocal = '${token}';
	query = encodeURIComponent(query);

	var queryUrl = '${request.contextPath}/query/query?querytext=' + query+"&token=" + tokenLocal;
	jQuery.ajax({
		type:'GET',
		url:queryUrl,
		success:function(r){
			if(r == '' && !(queryIsCancelled))
			getResults(tokenLocal, query);
			else {
				revertShowLoading();
				document.getElementById("errorMessage").innerHTML = r;
			}
			return false;
			},
		error:function(e){
			}
		});

}
</script>

</head>
<body>

<!--query input field -->

${Debug.printDelayV("begin GSP") }

	<h1 class="h1-href"><a href="../..${request.contextPath}/query/query?token=${token }">MoB4LOD</a></h1>
	
	<div id = "errorMessage">
    <br>
    </div>
    
    <g:if test="${request.errorOccurred }">
	${request.errorMessage }
	</g:if>
	
	<div id="loading" class="loading-invisible">
    	<p><img src="${resource(dir: 'images', file: 'loadingBar.gif')}" width="200" height="30" /></p>
    </div>
    
	<g:form name="querySubmit" action="query" onSubmit="return false;">
	    Enter keyword or URI:
		<richui:autoComplete name="querytext" id="querytextInput" style="width: 50%"
			action="${createLinkTo('dir': 'lookup/search')}"
			onItemSelect="asyncQuery(document.querySubmit.querytext.value)"
			title="Enter keyword or URI"/>
		<input type="submit" value="Query URI" onclick="asyncQuery(document.querySubmit.querytext.value)"  />
        <input type="hidden" name="token" value="${token }">
	</g:form>
	
	
	<script type="text/javascript">
	$(document).ready(function() {
	  	$(window).keydown(function(event){
	  	  if(event.keyCode == 13) {
	   	   event.preventDefault();
	   	   asyncQuery(document.querySubmit.querytext.value);
	   	   return false;
	 	   }
		  });
		});
	</script>
	
	<g:if test="${AppConfig.isEnableCancelButton()}">
		<button id="cancelB" class="cancelButton" onclick="cancelQueries('${token}')">Cancel Query</button>
	</g:if>
	<g:if test="${AppConfig.isEnableCancelAllButton()}">
	<button id="cancelAllB" class="cancelButton" onclick="cancelAllQueries()">Cancel All Queries</button>
	</g:if>
	
	 
	<div>
	<!-- don't worry about warnings about tags not closing: dynamically inserting divs confuses the static checker -->
			<g:if test="${request.renderers != null}">
				Results for "${Utils.utf8toUnicode(  request.querytext)}":
				<br>
				<br>
				
				<!-- begin actual rendering -->
				<!-- iterate over all renderers -->
				${Debug.printDelayV("before rendering") }
					<g:set var="j" value="${0}"/> 
					<g:each in="${request.renderers}">
						<%j++%>
						<g:if test="${it.isClusterStart()}">
							<g:if test="${it.getCaption() != null}">
								<hr class="captionTop">
								<p align="center">
									<b> ${it.getCaption()}
									</b>
								</p>
								<hr class="captionBottom">
							</g:if>
							<div class="rendererCluster">
						</g:if>
						<g:else>
							<g:if test="${it.isClusterEnd()}">
						</div>
						</g:if>
						<g:else>
							<g:if test="${it.getResult() != null && it.getResult().size() != 0}">
								<g:if test="${it.getCaption() != null}">
									<hr class="captionTop">
									<p align="center">
										<b> ${it.getCaption()}
										</b>
									</p>
									<hr class="captionBottom">
								</g:if>
								<div id="${it.getName()}">
								<!-- renderers are inserted here -->
								<g:render template="${it.getName()}" model="[result: it.getResult()]" />
								</div>
								<g:if test="${request.renderers.size() > j}">
								<g:if test="${!request.renderers.get(j).isClusterEnd() && !request.renderers.get(j).isClusterStart()}">
								<!-- set hr-tag if next RendererEntry is not end and not start of a cluster -->
							<hr>
				</g:if>
			</g:if>
		</g:if>
	</g:else>
	</g:else>
	</g:each>
	</g:if>
	
<script type="text/javascript">
	document.getElementById('querytextInput').focus();
 </script>

${Debug.printDelayV("end GSP") }
${Debug.stopTimer() }
${AppConfig.isDebugMode() ? System.out.println("---- query " + params.querytext + " end ----")  : ""}


</body>
</html>