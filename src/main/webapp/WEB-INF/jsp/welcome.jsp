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
    <div class="hero-unit"
      style="background-image: url('static/img/science.png'); background-repeat: no-repeat; background-position: 91% 100%; background-size: 21%;">
      <h1>BRBBio</h1>
      <p>This webapp is designed to make life easier for analyzing...stuff!</p>
    </div>

    <div class="row" style="margin-bottom:25px;">
      <div class="span12">
        <h2>Upload and manage data</h2>
      </div>
    </div>
    
    <div class="row">
      <div class="span2"></div>
      <div class="span3">
        <h4>Raw Upload</h4>
        <p>Upload raw screening data from Excel</p>
        <p>
          <a class="btn" href="rawUpload">Go &raquo;</a>
        </p>
      </div>
      <div class="span3">
        <h4>Viability Upload</h4>
        <p>Upload cytotoxicity data from Excel</p>
        <p>
          <a class="btn" href="viabilityUpload">Go &raquo;</a>
        </p>
      </div>
      <div class="span3">
        <h4>Delete a Run</h4>
        <p>Remove datasets from database</p>
        <p>
          <a class="btn" href="deleteRun">Go &raquo;</a>
        </p>
      </div>
    </div>

    <div class="row" style="margin-top:15px;margin-bottom:25px;">
      <div class="span12">
        <h2>View processed data</h2>
      </div>
    </div>

    <div class="row">
    <div class="span2"></div>
      <div class="span3">
        <h4>Normalization</h4>
        <p>Woo!</p>
        <p>
          <a class="btn" href="viewNormalizedData">Go &raquo;</a>
        </p>
      </div>
      <div class="span3">
        <h4>Z Factors</h4>
        <p>Aw yeah!</p>
        <p>
          <a class="btn" href="viewZFactor">Go &raquo;</a>
        </p>
      </div>
      <div class="span2">
        <h4>Cell Viability</h4>
        <p>Ohhhh!</p>
        <p>
          <a class="btn" href="viewViability">Go &raquo;</a>
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
