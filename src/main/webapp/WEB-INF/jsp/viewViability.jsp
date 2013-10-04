<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="container" ng-controller="DisplayController">
  <div id="selectBox">
    <h3>View Viability Data</h3>

    <table>
      <tr>
        <td>Run</td>
        <td><select ng-model="run" ng-change="getViabilityControls()">
            <option value="-1">[-Select-]</option>
            <c:forEach var="run" items="${runs}">
              <option value="<c:out value="${run.getId()}"/>">
                <c:out value="${run.getRunName()}" />
              </option>
            </c:forEach>
        </select><a href="b/getViabilityDataExcel?runId={{run}}&func={{encodeUrl(func)}}"><img src="assets/img/excel.png"></a><a href="b/getViabilityDataTsv?runId={{run}}&func={{encodeUrl(func)}}"><img
            src="assets/img/tsv.png"></a></td>
      </tr>

      <tr>
        <td>Formula</td>
        <td><textarea ng-model="func" ng-init="func = 'rawData/AVG(negativecontrol)'"></textarea></td>
      </tr>

      <tr>
        <td><button class="btn" ng-click="getViabilityData()">Process</button></td>
        <td><img class="loading" src="assets/img/loader.gif" ng-show="loading" /><span class="failure">{{loadError}}</span></td>
      </tr>
    </table>
  </div>

  <div id="legend">
    <h4>Information</h4>
    Cell Viability allows you to normalize the viability upload (linked or independent) by applying any basic Excel-style formula using the available controls and available functions for the run you
    have selected in the formula box.
    <div style="padding-left: 5px;">
      <dl>
        <dt>Available functions</dt>
        <dd>AVG(): Average of field</dd>
        <dd>STD(): Standard deviation of field</dd>
        <dd>MIN(): Lowest value of field</dd>
        <dd>MAX(): Largest value of field</dd>
      </dl>
      <dl>
        <dt>Available fields</dt>
        <dd>rawData: value of the Data column of the cytotoxicity spreadsheet</dd>
      </dl>
      <dl>
        <dt>Available controls</dt>
        <dd ng-repeat="control in controls">{{control}}</dd>
      </dl>
    </div>
  </div>

  <br />

  <div class="row">
    <div id="processed" class="span6">
      <table ng-show="showingData" class="table table-striped">
        <thead>
          <tr>
            <th>Plate ID</th>
            <th>Gene</th>
            <th>Viability</th>
          </tr>
        </thead>
        <tbody>
          <tr ng-repeat="row in rows">
            <td>{{row.plate}}</td>
            <td>{{row.gene}}</td>
            <td>{{row.data}}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>

<!--script>
  function displayProcessedData(data, runId) {
    $('#processed').children().remove();

    var tbl = $('<table class="table table-striped"><thead><tr><th>Plate ID</th><th>Gene</th><th>Viability</th></tr></thead><tbody></tbody></table>');
    $.each(data, function() {
      tbl.append('<tr><td>' + this.plateName + '</td><td>' + this.geneId + '</td><td>' + this.normalized + '</td></tr>');
    });

    $('#processed').append(tbl);
    hideLoading();
  }

  function displayControlsForRun(data) {
    $('#controls').children().remove();
    $.each(data, function() {
      $('#controls').append('<option>' + this + '</option>');
    });
  }

  function showLoading() {
    $('#selectBox table tr:nth-child(3) td:last-child').html('<img class="loading" src="static/img/loader.gif"/>');
    $('#selectBox table button').text('Loading...').attr('disabled', true);
  }

  function hideLoading() {
    $('#selectBox table tr:nth-child(3) td:last-child').html('');
    $('#selectBox table button').text('Show Data').attr('disabled', false);
  }

  function getFormula() {
    return encodeURIComponent($('#func').val());
  }

  $(document).ready(function() {
    $('#run').change(function() {
      var id = $(this).val();
      $('#processed').children().remove();
      $('#selectBox span').remove();

      $.get('getViabilityControlsForRun', {
        runId : id
      }, function(data) {
        displayControlsForRun(data);
      });

      $('#excel').attr('href', 'getViabilityDataExcel?runId=' + $(this).val() + '&func=' + getFormula());
      $('#tsv').attr('href', 'getViabilityDataTsv?runId=' + $(this).val() + '&func=' + getFormula());

      $('#selectBox button').click(function() {
        showLoading();
        $.get('getViabilityData', {
          runId : id,
          func : $('#func').val()
        }, function(data) {
          displayProcessedData(data, id);
        });
      });
    });
  });
</script-->