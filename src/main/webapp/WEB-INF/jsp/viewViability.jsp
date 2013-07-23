<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<html>
<head>
<title>View Data</title>
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap.min.css">
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap-responsive.css">
<link rel="stylesheet" href="static/css/style.css">
<link rel="stylesheet" href="static/css/jquery.fileupload-ui.css">
<link rel="stylesheet" href="static/css/brb.css">
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script src="http://blueimp.github.com/cdn/js/bootstrap.min.js"></script>
<script>

function displayProcessedData(data, runId){
	$('#processed').children().remove();
	
	var tbl = $('<table class="table table-striped"><thead><tr><th>Plate ID</th><th>Gene</th><th>Viability</th></tr></thead><tbody></tbody></table>');
	$.each(data, function(){
		tbl.append('<tr><td>'+this.plateName+'</td><td>'+this.geneId+'</td><td>'+this.normalized+'</td></tr>');
	});
	
	$('#processed').append(tbl);
	hideLoading();
}

function displayControlsForRun(data){
	$('#controls').children().remove();
	$.each(data, function(){
		$('#controls').append('<option>'+this+'</option>');
	});
}

function showLoading(){
	$('#selectBox table tr:nth-child(3) td:last-child').html('<img class="loading" src="static/img/loader.gif"/>');
	$('#selectBox table button').text('Loading...').attr('disabled', true);
}

function hideLoading(){
	$('#selectBox table tr:nth-child(3) td:last-child').html('');
	$('#selectBox table button').text('Show Data').attr('disabled', false);
}

function getFormula(){
	return encodeURIComponent($('#func').val());
}

$(document).ready(function(){
	$('#run').change(function(){
		var id = $(this).val();
		$('#processed').children().remove();
		$('#selectBox span').remove();
		
		$.get('getViabilityControlsForRun', {runId:id}, function(data){
			displayControlsForRun(data);
		});
		
		$('#excel').attr('href','getViabilityDataExcel?runId='+$(this).val()+'&func='+getFormula());
		$('#tsv').attr('href','getViabilityDataTsv?runId='+$(this).val()+'&func='+getFormula());
		
		$('#selectBox button').click(function(){
			showLoading();
    		$.get('getViabilityData', {runId:id,func:$('#func').val()}, function(data){
    			displayProcessedData(data, id);
    		});
		});
	});
});

</script>
</head>
<body>
  <jsp:include page="headers.jsp" />

  <div class="container">
    <div id="selectBox">
      <h3>View Viability Data</h3>

      <table>
        <tr>
          <td>Run</td>
          <td><select id="run">
              <option value="">[-Select-]</option>
              <c:forEach var="run" items="${runs}">
                <option value="<c:out value="${run.getId()}"/>">
                  <c:out value="${run.getRunName()}" />
                </option>
              </c:forEach>
          </select><a id="excel" href="#"><img src="static/img/excel.png"></a><a id="tsv" href="#"><img src="static/img/tsv.png"></a></td>
        </tr>

        <tr>
          <td>Formula</td>
          <td><textarea id="func">rawData/AVG(negativecontrol)</textarea></td>
        </tr>

        <tr>
          <td><button class="btn">Show data</button></td>
          <td></td>
        </tr>
      </table>
    </div>
        
    <div id="legend">
      <h4>Available functions</h4>
      <p>AVG(): Average of field</p>
      <p>STD(): Standard deviation of field</p>
      <p>MIN(): Lowest value of field</p>
      <p>MAX(): Largest value of field</p>
      <h4>Available fields</h4>
      <p>rawData: value of the Data column of the cytotoxicity spreadsheet</p>
      <h4>Available controls</h4>
      <select id="controls" multiple disabled></select>
    </div>
    
    <br />

    <div class="row">
      <div id="processed" class="span6"></div>
    </div>
  </div>
</body>
</html>