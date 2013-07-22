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
#controls li{
  list-style-type: none;
}
table td{
  padding:10px;
}
input[type="text"]{
  display:block;
  height:24px;
}
button{
  position:relative;
  top: -5px;
}
.ctrlAdd{
  margin-left:10px;
}
</style>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script src="http://blueimp.github.com/cdn/js/bootstrap.min.js"></script>
<script src="static/js/jquery.iframe-transport.js"></script>
<script src="static/js/jquery.fileupload.js"></script>
<script>
    function addControl(){
    	$('#controls').append('<input name="ctrl" type="text" />');
    }

	$(document).ready(
			function() {
				'use strict';

				$('#fileupload').fileupload({
					dataType : 'json',
					maxNumberOfFiles : 1,
					submit : function(e, data) {
						var controls  = [];
						$('input[name="ctrl"]').each(function(){
							if($(this).val() != ''){
								controls.push($(this).val());
							}
						});
						
						$('#fileupload').fileupload(
								'option',
								'url',
								'doViabilityLoad?runId='+ $('#run').val()+'&controls='+escape(controls.join()));
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
    <span id="files" class="files">Please select a run and submit a file.</span>
    <!-- The global progress bar -->
    <span id="progress" style="display: none"> <img src="static/img/loader.gif" />
    </span>

    <table>
      <tr>
        <td>Run</td>
        <td>
          <!-- The fileinput-button span is used to style the file input field as button --> <select id="run">
            <option value="">[-Select-]</option>
            <c:forEach var="run" items="${runs}">
              <option value="<c:out value="${run.getId()}"/>">
                <c:out value="${run.getRunName()}" />
              </option>
            </c:forEach>
        </select>
        </td>
        <td><span class="btn btn-success fileinput-button"> <i class="icon-plus icon-white"></i> <span>Upload</span>
            <!-- The file input field used as target for the file upload widget --> <input id="fileupload" type="file"
            name="files[]" multiple>
        </span></td>
      </tr>
      <tr>
        <td>Controls:</td>
        <td>
          <div id="controls">
            <input name="ctrl" type="text" />
          </div>
        </td>
        <td>
          <button class="btn ctrlAdd" onclick="addControl()">
            <i class="icon-plus icon-gray"></i> Add
          </button>
        </td>
      </tr>
    </table>

  </div>

</body>
</html>