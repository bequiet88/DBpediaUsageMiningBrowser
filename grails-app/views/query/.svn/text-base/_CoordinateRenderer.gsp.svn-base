<!--  renders coordinates on google maps -->
<!--  targettype is table -->
<%@page import="utils.Utils" %> 
<%@page import="result.Result" %> 
<%@page import="result.ResultComposite" %> 
<%@page import="result.Table" %>
<%@page import="view.CoordinateReader" %> 
<%@page import="view.Coordinate" %>

<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

<script type="text/javascript"
    src="https://maps.google.com/maps/api/js?sensor=false">
</script>
<!-- functions for google maps -->
<script type="text/javascript">
  function initialize(lat, lon, resName) {
	if (lat != -1) {
    	var latlng = new google.maps.LatLng(lat, lon);
    	var myOptions = {
     	 zoom: 8,
     	 center: latlng,
     	 mapTypeId: google.maps.MapTypeId.ROADMAP
   	 	};
   		var map = new google.maps.Map(document.getElementById("map_canvas"),
        	myOptions);
   		var marker = new google.maps.Marker({
      	  position: latlng, 
      	  map: map, 
      	  title:resName
   		}); 
	} else {
		var text = document.createTextNode("Leider keine Koordinaten verfügbar.");
	    document.getElementById("map_canvas").appendChild(text);
	}
  }

</script>
<!-- reading coordinates from the given table via CoordinateReader -->
<g:set var="coordinateReader" value="${new CoordinateReader((Table) result)}"/>
<g:set var="coordinates" value="${coordinateReader.readCoordinates()}"/>

<g:if test="${coordinates.size() > 0}">
	<g:set var="lat" value="${coordinates.get(0).getLatitude()}"/>
	<g:set var="lon" value="${coordinates.get(0).getLongitude()}"/>
	<g:set var="resName" value="${coordinates.get(0).getResName()}"/>
</g:if>
<g:else>
	${System.out.println("No coordinates found!") }
	<g:set var="lat" value="${-1}"/>
	<g:set var="lon" value="${-1}"/>
	<g:set var="resName" value="${0}"/>
</g:else>

<!-- actual element where the map is inserted -->
<div class="divCentered">
	<div class="map" id="map_canvas" ></div>
</div>
<!-- call for initialization -->
<script type="text/javascript">
	window.onload = initialize(${lat}, ${lon}, '${resName }');
</script>

