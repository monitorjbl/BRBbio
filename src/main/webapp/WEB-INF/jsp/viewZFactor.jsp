<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="//cdn.jsdelivr.net/jqplot/1.0.8/jquery.jqplot.css">
<script src="//code.highcharts.com/highcharts.js"></script>

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
        <td><img class="loading" src="img/loader.gif" ng-show="loading" /></td>
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
    <div class="span9" >
      <div>&nbsp;</div>
      <div id="chart" ng-show="showingData"></div>
    </div>
  </div>

  <br />

  <div class="row">
    <div class="span6">
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