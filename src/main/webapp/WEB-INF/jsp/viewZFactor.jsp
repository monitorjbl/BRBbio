<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="container" ng-controller="DisplayController">
  <div id="selectBox">
    <h3>View Z Factors</h3>
    <table>
      <tr>
        <td>Run</td>
        <td><select ng-model="run" ng-change="getControls()">
            <option value="-1">[-Select-]</option>
            <c:forEach var="run" items="${runs}">
              <option value="<c:out value="${run.getId()}"/>">
                <c:out value="${run.getRunName()}" />
              </option>
            </c:forEach>
        </select><a id="excel" href="b/getZFactorExcel?runId={{run}}&func={{encodeUrl(func)}}"><img src="img/excel.png"></a><a id="tsv" href="b/getZFactorTsv?runId={{run}}&func={{encodeUrl(func)}}"><img
            src="img/tsv.png"></a></td>
      </tr>

      <tr>
        <td>Formula</td>
        <td><textarea ng-model="func">{{func = '1-(3*(STD(negativecontrol) + STD(Copb1_indi)))/(AVG(negativecontrol)-AVG(Copb1_indi))'}}</textarea></td>
      </tr>

      <tr>
        <td><button class="btn" ng-click="getZFactor()">Show data</button></td>
        <td><img class="loading" src="img/loader.gif" ng-show="loading"/></td>
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
    <p>No fields are available on this view</p>
    <h4>Available controls</h4>
    <select id="controls" multiple disabled>
      <option ng-repeat="control in controls">{{control}}</option>
    </select>
  </div>

  <br />

  <div class="row">
    <div id="processed" class="span6">
      <table ng-show="showingData" class="table table-striped">
        <thead>
          <tr>
            <th>Plate ID</th>
            <th ng-repeat="hour in hours">{{hour}}</th>
          </tr>
        </thead>
        <tbody>
          <tr ng-repeat="row in rows">
            <td>{{row.plate}}</td>
            <td ng-repeat="hour in hours">{{row.data[$index]}}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <div class="row">
    <div id="processed" class="span6"></div>
  </div>

</div>

<!--script>
  function displayProcessedData(data, runId) {
    $('#processed').children().remove();

    var tbl = $('<table class="table table-striped"><thead><tr><th>Plate ID</th></tr></thead><tbody></tbody></table>');
    var time = {};
    var tableRows = {};
    $.each(data, function() {
      var key = this.plateName + '_' + this.geneId;
      if (tableRows[key] == undefined) {
        tableRows[key] = {
          plate : this.plateName,
          data : []
        };
      }
      tableRows[key].data.push(this.zFactor);
      time['_' + this.timeMarker] = true;
    });

    for (key in time) {
      tbl.find('thead tr').append('<th>' + key.substring(1) + 'hr</th>');
    }

    for (key in tableRows) {
      var r = tableRows[key];
      var row = $('<tr><td>' + r.plate + '</td></tr>');

      if (Object.keys(time).length - r.data.length == 0) {
        $.each(r.data, function(i, j) {
          row.append('<td>' + j + '</td>');
        });
      } else {
        row.append('<td colspan="' + Object.keys(time).length + '" class="badData">Improper data found</td>');
      }

      tbl.append(row);
    }

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

      $.get('getRawDataControlsForRun', {
        runId : id
      }, function(data) {
        displayControlsForRun(data);
      });

      $('#excel').attr('href', 'getZFactorExcel?runId=' + $(this).val() + '&func=' + getFormula());
      $('#tsv').attr('href', 'getZFactorTsv?runId=' + $(this).val() + '&func=' + getFormula());

      $('#selectBox button').click(function() {
        showLoading();
        $.get('getZFactorData', {
          runId : id,
          func : $('#func').val()
        }, function(data) {
          displayProcessedData(data, id);
        });
      });
    });
  });
</script-->
