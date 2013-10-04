<div class="row" ng-controller="RawCtrl">
  <div class="span10">
    <h3>Upload raw data</h3>

    <form destination="b/doRawDataLoad" returnPath="/" name="form" upload-form novalidate>
      <table style="width: 500px;">
        <tr>
          <td>Run name</td>
          <td><input type="text" ng-model="runName" name="runName" required /></td>
        </tr>
        <tr>
          <td>Excel file</td>
          <td><input type="text" ng-model="filename" disabled style="width: 100px; display: inline;" required /> <span class="btn btn-success fileinput-button"> <i
              class="icon-plus icon-white"></i> <span>Choose</span><input type="file" name="file" ng-model="fileUpload" onchange="angular.element(this).scope().updateFilename(this)"></span></td>
        </tr>
        <tr>
          <td>Control (positive)</td>
          <td><input name="control" type="text" ng-model="controlPos" class="input-medium" required /></td>
        </tr>
        <tr>
          <td>Control (negative)</td>
          <td><input name="control" type="text" ng-model="controlNeg" class="input-medium" required /></td>
        </tr>
        <tr>
          <td style="vertical-align: top" colspan="2">
            <button class="btn ctrlAdd" style="display: block;" ng-click="addControl()">
              <i class="icon-plus"></i>Add Control
            </button>
          </td>
        </tr>
        <tr ng-repeat="control in controls">
          <td>Control</td>
          <td><input name="control" type="text" ng-model="control.val" class="input-medium" required />
            <button class="btn btn-danger" style="position: relative; top: -5px; margin-left: 10px;" ng-click="remove($index)">
              <i class="icon-ban-circle icon-white"></i>
            </button></td>
        </tr>

        <tr>
          <td>&nbsp;</td>
        </tr>

      </table>
      <button type="button" class="btn btn-primary start" ng-disabled="form.$invalid || loading" ng-click="upload()">
        <i class="icon-upload icon-white"></i> <span>Start Upload</span>
      </button>
      <img src="assets/img/loader.gif" ng-show="loading" /> <span class="failure">{{loadError}}</span>
    </form>

  </div>
  <div id="legend">
    <h4>How To</h4>
    <ol>
      <li>Select a name for this data set</li>
      <li>Choose your Excel data, in the correct <a href="templates/brb_template.xlsx">format</a>
        <ul>
          <li>AssayPlate: Usually the plate # or ID</li>
          <li>Identifier: Usually the Gene ID</li>
          <li>TimeMarker: The TimeMarker, has to be a number. This can also just be 0 if only one time point</li>
        </ul></li>
      <li>Define as many controls as you like
        <ul>
          <li>Note: Each plate screened must contain these controls</li>
        </ul>
      </li>
      <li>Click Start Upload</li>
    </ol>
  </div>
</div>