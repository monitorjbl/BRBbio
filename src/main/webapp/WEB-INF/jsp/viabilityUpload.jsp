<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><!DOCTYPE html>
<div class="row">
  <div class="span10" ng-controller="RawCtrl">
    <h3>Upload raw data</h3>

    <form destination="b/doViabilityLoad" returnPath="/" name="form" upload-form novalidate>
      <table style="width: 400px;">
        <tr>
          <td>Run</td>
          <td><select name="runId" ng-model="run" required>
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
          <td style="vertical-align: top" colspan="2">
            <button class="btn ctrlAdd" style="display: block;" ng-click="addControl()">
              <i class="icon-plus icon-gray"></i>Add Control
            </button>
          </td>
        </tr>

        <tr ng-repeat="item in items">
          <td>Control</td>
          <td><input name="control" type="text" ng-model="control" required /></td>
        </tr>


      </table>
      <button type="button" class="btn btn-primary start" ng-disabled="form.$invalid" ng-click="upload()">
        <i class="icon-upload icon-white"></i> <span>Start upload</span>
      </button>
    </form>
  </div>
</div>
