<html>
<head>
<title>Upload Excel Data</title>
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap.min.css">
<link rel="stylesheet" href="static/css/style.css">
<link rel="stylesheet" href="static/css/jquery.fileupload-ui.css">
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script src="static/js/jquery.iframe-transport.js"></script>
<script src="static/js/jquery.fileupload.js"></script>
<script>
/*jslint unparam: true */
/*global window, $ */
$(document).ready(function(){
    'use strict';
    
    
    $('#fileupload').fileupload({
        dataType: 'json',
        maxNumberOfFiles: 1,
        submit: function (e, data) {
        	$('#fileupload').fileupload( 'option',
        		    'url',
        		    'doLoad?runName='+escape($('#runName').val()));
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
  Run name: <input type="text" id="runName" />
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