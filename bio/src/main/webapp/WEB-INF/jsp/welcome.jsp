<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>BRBBio</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<!-- Le styles -->
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap.min.css">
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}
</style>
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap-responsive.css">

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="../assets/js/html5shiv.js"></script>
    <![endif]-->
</head>

<body>
  <jsp:include page="headers.jsp" />

  <div class="container">

    <!-- Main hero unit for a primary marketing message or call to action -->
    <div class="hero-unit" style="background-image: url('static/img/science.png');background-repeat:no-repeat;background-position: 91% 100%;background-size:21%;">
      <h1>For Science!</h1>
      <p>This is webapp designed to make life easier for analyzing...stuff!</p>
    </div>

  <!-- Example row of columns -->
    <div class="row">
      <div class="span3">
        <h2>Excel Upload</h2>
        <p>Fucking Excel!</p>
        <p>
          <a class="btn" href="excelUpload">Go &raquo;</a>
        </p>
      </div>
      <div class="span3">
        <h2>Delete a Run</h2>
        <p>Get that shit out of here!</p>
        <p>
          <a class="btn" href="deleteRun">Go &raquo;</a>
        </p>
      </div>
      <div class="span3">
        <h2>Normalization</h2>
        <p>Hot damn!</p>
        <p>
          <a class="btn" href="viewNormalizedData">Go &raquo;</a>
        </p>
      </div>
      <div class="span3">
        <h2>Z Factors</h2>
        <p>Mathematical!</p>
        <p>
          <a class="btn" href="viewZFactor">Go &raquo;</a>
        </p>
      </div>
    </div>

    <hr>

    <footer>
      <!-- p>&copy; Company 2013</p-->
    </footer>

  </div>
  <!-- /container -->

  <!-- Le javascript
    ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
  <script src="http://blueimp.github.com/cdn/js/bootstrap.min.js"></script>

</body>
</html>
