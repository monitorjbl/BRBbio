<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><!DOCTYPE html>
<html>
<head>
<title>Delete Run</title>
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap.min.css">
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap-responsive.css">
<link rel="stylesheet" href="static/css/style.css">
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script>
	function deleteRun() {
		if (confirm("Are you sure you want to delete this run's data? This cannot be undone.")) {
			$.get('deleteRunById?runId=' + $('#run').val(), function(data) {
				location.reload();
			});
		}
	}
</script>
</head>
<body>
  <jsp:include page="headers.jsp" />
  
    <div class="container">
  <div id="selectBox">
    <h3>Delete Run</h3>

    Run: <select id="run">
      <option value="">[-Select-]</option>
      <c:forEach var="run" items="${runs}">
        <option value="<c:out value="${run.getId()}"/>">
          <c:out value="${run.getRunName()}" />
        </option>
      </c:forEach>
    </select>
    <button onclick="deleteRun()">Delete</button>
  </div>
  </div>

</body>
</html>