<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<html>
<head>
<title>View Data</title>
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap.min.css">
<link rel="stylesheet" href="static/css/style.css">
<link rel="stylesheet" href="static/css/jquery.fileupload-ui.css">
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script>

$(document).ready(function(){
	$('#plate').change(function(){
		console.log(this);
		$.get('getRawDataForPlate?plateId='+$(this).val(), function(data){
			console.log(data);
		})
	})
});

</script>
</head>
<body>

<h3>View Data</h3>

Plate ID: 
<select id="plate">
  <c:forEach var="plate" items="${plates}">
   <option value="<c:out value="${plate.getId()}"/>"><c:out value="${plate.getPlateName()}"/></option>
  </c:forEach>
</select>

</body>
</html>