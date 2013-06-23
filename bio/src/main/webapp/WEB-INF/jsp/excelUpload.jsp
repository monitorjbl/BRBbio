<html>
<head>
<!-- Bootstrap CSS Toolkit styles -->
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap.min.css">
<!-- Generic page styles -->
<link rel="stylesheet" href="static/css/style.css">
<!-- Bootstrap styles for responsive website layout, supporting different screen sizes -->
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap-responsive.min.css">
<!-- Bootstrap CSS fixes for IE6 -->
<!--[if lt IE 7]><link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap-ie6.min.css"><![endif]-->
<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
<link rel="stylesheet" href="static/css/jquery.fileupload-ui.css">
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<!-- cript src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script-->
<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="static/js/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="static/js/jquery.fileupload.js"></script>
<script>
/*jslint unparam: true */
/*global window, $ */
$(document).ready(function(){
    'use strict';
    
    
    $('#fileupload').fileupload({
        url: 'doLoad',
        dataType: 'json',
        maxNumberOfFiles: 1,
        formData: {name: 'test', value:'boobs'},
        submit: function (e, data) {
        	console.log(data);
        	$('#files').html('');
            $('#progress').show();
        },
        done: function (e, data) {
            $.each(data.result.files, function (index, file) {
            	$('#progress').fadeOut(1000);
                $('<p/>').text('Uploaded '+file.name+' successfully').appendTo('#files');
                
            });
        },
        fail: function(e, data){
        	$('#progress').fadeOut(1000);
            $('<p/>').text('Failed to upload file').appendTo('#files');
        },
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .bar').css(
                'width',
                progress + '%'
            );
        }
    });
});
</script>
<style type="text/css">
#progress{
  width:100px;
}
#files{
  margin-bottom:10px;
}
</style>
</head>
<body>
<h3>Upload Excel data</h3>

  <!-- The container for the uploaded files -->
  <div id="files" class="files">No files uploaded</div>
  <!-- The fileinput-button span is used to style the file input field as button -->
  <input type="text" />
  <span class="btn btn-success fileinput-button"> <i class="icon-plus icon-white"></i> <span>Upload</span> <!-- The file input field used as target for the file upload widget --> <input id="fileupload"
    type="file" name="files[]" multiple>
  </span>
  <br>
  <br>
  <!-- The global progress bar -->
  <div id="progress" class="progress progress-success progress-striped">
    <div class="bar"></div>
  </div>
  


</body>
</html>