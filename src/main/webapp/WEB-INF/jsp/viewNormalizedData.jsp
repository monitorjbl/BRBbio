<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
  td, th {
    text-align: left;
  }
</style>
<div class="container" ng-controller="DisplayController">
  <div id="selectBox">
    <h3>View Normalized Data</h3>

    <table>
      <tr>
        <td>Run</td>
        <td><select ng-model="run" ng-change="getRawControls()">
          <option value="-1">[-Select-]</option>
          <c:forEach var="run" items="${runs}">
            <option value="<c:out value="${run.getId()}"/>">
              <c:out value="${run.getRunName()}"/>
            </option>
          </c:forEach>
        </select><a id="excel" href="b/getNormalizedDataExcel?runId={{run}}&func={{encodeUrl(func)}}"><img
                src="assets/img/excel.png"></a><a id="tsv"
                                                  href="b/getNormalizedDataTsv?runId={{run}}&func={{encodeUrl(func)}}"><img
                src="assets/img/tsv.png"></a></td>
      </tr>

      <tr>
        <td>Formula</td>
        <td><textarea ng-model="func"
                      ng-init="func = '(rawData/AVG(Copb1_indi))/(AVG(negativecontrol)/AVG(Copb1_indi))'"></textarea>
        </td>
      </tr>

      <tr>
        <td>
          <button class="btn" ng-click="getNormalizedData()">Process</button>
        </td>
        <td><img class="loading" src="assets/img/loader.gif" ng-show="loading"/><span
                class="failure">{{loadError}}</span>
        </td>
      </tr>
    </table>
  </div>
  <br/>

  <div id="legend">
    <h4>Information</h4>
    Normalization allows you to apply any basic Excel-style formula using the available controls and available
    functions for the run you have selected in the formula box.
    <div style="padding-left:5px;">
      <dl>
        <dt>Available functions</dt>
        <dd>AVG(): Average of field</dd>
        <dd>STD(): Standard deviation of field</dd>
        <dd>MIN(): Lowest value of field</dd>
        <dd>MAX(): Largest value of field</dd>
      </dl>
      <dl>
        <dt>Available fields</dt>
        <dd>rawData: value of the Data column of the raw data spreadsheet</dd>
      </dl>
      <dl>
        <dt>Available controls</dt>
        <dd ng-repeat="control in controls">{{control}}</dd>
      </dl>
    </div>
  </div>

  <div class="row">
    <div id="processed" class="span8">
      <table ng-show="showingData" class="table table-striped">
        <thead>
        <tr>
          <th>Plate ID</th>
          <th>Entrez Gene ID</th>
          <th>Gene Symbol</th>
          <th ng-repeat="hour in hours">{{hour}}</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="row in rows">
          <td>{{row.plate}}</td>
          <td>{{row.geneId}}</td>
          <td>{{row.geneSymbol}}</td>
          <td ng-repeat="hour in hours">{{row.data[$index]}}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>