<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<html>
<head>
<title>View Data</title>
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap.min.css">
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap-responsive.css">
<link rel="stylesheet" href="static/css/style.css">
<link rel="stylesheet" href="static/css/jquery.fileupload-ui.css">
<style type="text/css">
#selectBox{
  margin-left:10px;
}
#processed table{
  margin-left:50px;
}
#selectBox button{
  width:100px;
}
table td{
  padding:5px;
}
textarea{
  width:100%;
  height:100px;
}
a{
  padding-left:10px;
}
</style>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script src="http://blueimp.github.com/cdn/js/bootstrap.min.js"></script>
<script>

function displayProcessedData(data, runId){
	$('#processed').children().remove();
	
	var tbl = $('<table class="table table-striped"><thead><tr><th>Plate ID</th><th>Gene</th></tr></thead><tbody></tbody></table>');
	var time = {};
	var tableRows = {};
	$.each(data, function(){
		var key = this.plateName+'_'+this.geneId;
		if(tableRows[key] == undefined){
			tableRows[key] = {
				plate:this.plateName,
				gene:this.geneId,
				data:[]
			};
		}
		tableRows[key].data.push(this.normalized);
		time['_'+this.timeMarker] = true;
	});
	
	for(key in time){
		tbl.find('thead tr').append('<th>'+key.substring(1)+'hr</th>');
	}
	
	for(key in tableRows){
		var r = tableRows[key];
		var row = $('<tr><td>'+r.plate+'</td><td>'+r.gene+'</td></tr>');
		$.each(r.data, function(i,j){
			row.append('<td>'+j+'</td>');
		});
		
		tbl.append(row);
	}
	
	$('#processed').append(tbl);
	hideLoading();
}

function showLoading(){
	$('#selectBox table tr:nth-child(3) td:last-child').html('<img class="loading" src="static/img/loader.gif"/>');
	$('#selectBox table button').text('Loading...').attr('disabled', true);
}

function hideLoading(){
	$('#selectBox table tr:nth-child(3) td:last-child').html('');
	$('#selectBox table button').text('Show Data').attr('disabled', false);
}

$(document).ready(function(){
	$('#run').change(function(){
		var id = $(this).val();
		$('#processed').children().remove();
		$('#selectBox span').remove();
		
		$('#selectBox a').attr('href','getNormalizedDataExcel?runId='+$(this).val()+'&func='+$('#func').val());
		$('#selectBox button').click(function(){
			showLoading();
    		$.get('getNormalizedData', {runId:id,func:$('#func').val()}, function(data){
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
      <h3>View Normalized Data</h3>

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
          </select><a id="excel" href="#"><img src="static/img/excel.png"></a></td>
        </tr>

        <tr>
          <td>Function</td>
          <td><textarea id="func">(rawData/AVG(Copb1_indi))/(AVG(negativecontrol)/AVG(Copb1_indi))</textarea></td>
        </tr>

        <tr>
          <td><button class="btn">Show data</button></td>
          <td></td>
        </tr>
      </table>
    </div>
    <br />
    
    <div class="row">
      <div id="processed" class="span6"></div>
    </div>
  </div>
</body>
</html>