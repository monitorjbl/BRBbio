<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<html>
<head>
<title>Upload Excel Data</title>
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap.min.css">
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap-responsive.css">

<link rel="stylesheet" href="static/css/style.css">
<link rel="stylesheet" href="static/css/jquery.fileupload-ui.css">
<style type="text/css">
#progress {
  width: 100px;
}

#files {
  margin-bottom: 10px;
}
</style>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script src="http://blueimp.github.com/cdn/js/bootstrap.min.js"></script>
<script src="static/js/jquery.iframe-transport.js"></script>
<script src="static/js/jquery.fileupload.js"></script>
<script>
	/*jslint unparam: true */
	/*global window, $ */
	$(document).ready(
			function() {
				'use strict';

				$('#fileupload').fileupload({
					dataType : 'json',
					maxNumberOfFiles : 1,
					submit : function(e, data) {
						$('#fileupload').fileupload(
								'option',
								'url',
								'doViabilityLoad?runId='+ $('#run').val());
						$('#files').html('Uploading...');
						$('#progress').show();
					},
					done : function(e, data) {
						$.each(data.result.files,
								function(index, file) {
									$('#progress').hide();
									$('#files').html('Uploaded ' + file.name+ ' successfully');
								});
					},
					fail : function(e, data) {
						$('#progress').fadeOut(1000);
						$('<p/>').text('Failed to upload file').appendTo('#files');
					}
				});
			});
</script>
</head>
<body>
  <jsp:include page="headers.jsp" />

  <div class="container">
    <h3>Upload viability data</h3>

    <!-- The container for the uploaded files -->
    <span id="files" class="files">Please select a run and submit a file. If your viability data contains plates that the run did not have, they will be added without controls. This means that they will not appear in the viability report.</span>
    <!-- The global progress bar -->
    <span id="progress" style="display: none"> <img src="static/img/loader.gif" />
    </span>
    <!-- The fileinput-button span is used to style the file input field as button -->
    <div style="padding-top: 10px;">
      Run: <select id="run">
        <option value="">[-Select-]</option>
        <c:forEach var="run" items="${runs}">
          <option value="<c:out value="${run.getId()}"/>">
            <c:out value="${run.getRunName()}" />
          </option>
        </c:forEach>
      </select> <span class="btn btn-success fileinput-button"> <i class="icon-plus icon-white"></i> <span>Upload</span> <!-- The file input field used as target for the file upload widget -->
        <input id="fileupload" type="file" name="files[]" multiple>
      </span>
    </div>

  </div>

</body>
</html>