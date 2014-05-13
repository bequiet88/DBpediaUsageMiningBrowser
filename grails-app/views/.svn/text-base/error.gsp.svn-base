<%@page import="utils.AppConfig"%>

<!doctype html>
<html>
	<head>
		<title>Grails Runtime Exception</title>
		<meta name="layout" content="main">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'errors.css')}" type="text/css">
		<g:if test="${AppConfig.isDebugMode() == false}">
			<% request.results = null %>
			<% request.errorOccurred = true %>
			<% request.errorMessage = "Internal Error: " + "${exception}" %>
			<meta http-equiv="REFRESH" content="0;url=/LDB/query/index"/>
		</g:if>
	</head>
	<body>
		<g:if test="${AppConfig.isDebugMode()}">
			<g:renderException exception="${exception}" />
		</g:if>
	</body>
</html>