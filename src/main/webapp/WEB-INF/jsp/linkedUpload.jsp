<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><!DOCTYPE html>
<div class="row">
  <div class="span10" ng-controller="RawCtrl">
    <h3>Upload linked viability data</h3>

    <form destination="b/doLinkedViabilityLoad" returnPath="/" name="form" upload-form novalidate>
      <table style="width: 400px;">
        <tr>
          <td>Run</td>
          <td><select name="runId" ng-model="runId" ng-change="getRun(runId)" required>
              <option value="">[-Select-]</option>
              <c:forEach var="run" items="${runs}">
                <option value="<c:out value="${run.getId()}"/>">
                  <c:out value="${run.getRunName()}" />
                </option>
              </c:forEach>
          </select></td>
        </tr>
        <tr>
          <td>Excel file</td>
          <td><input type="text" ng-model="filename" disabled style="width: 100px; display: inline;" required /> <span class="btn btn-success fileinput-button"> <i
              class="icon-plus icon-white"></i> <span>Choose</span><input type="file" name="file" ng-model="fileUpload" onchange="angular.element(this).scope().updateFilename(this)"></span></td>
        </tr>

        <tr>
          <td>Controls</td>
          <td><table>
              <tr ng-repeat="control in controls">
                <td><input type="text" ng-model="control.val" disabled /></td>
              </tr>
            </table></td>
        </tr>

        <tr>
          <td>&nbsp;</td>
        </tr>
      </table>
      <button type="button" class="btn btn-primary start" ng-disabled="form.$invalid" ng-click="upload()">
        <i class="icon-upload icon-white"></i> <span>Start Upload</span>
      </button>
      <img src="img/loader.gif" ng-show="loading" /> <span class="failure">{{loadError}}</span>
    </form>
  </div>
  <div id="legend">
    <h4>How To</h4>
    <ol>
      <li>Select a raw data set that this is linked with
        <ul>
          <li>The controls from your raw data will be used</li>
        </ul>
      </li>
      <li>Choose your Excel data, in the correct <a href="templates/brb_template.xlsx">format</a>
        <ul>
          <li>AssayPlate: Usually the plate # or ID</li>
          <li>Identifier: Usually the Gene ID</li>
          <li>TimeMarker: The TimeMarker, has to be a number. This can also just be 0 if only one time point</li>
        </ul></li>
      </li>
      <li>Click Start Upload</li>
    </ol>
  </div>
</div>
