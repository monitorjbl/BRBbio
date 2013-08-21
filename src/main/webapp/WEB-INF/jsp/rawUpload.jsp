<div class="row"ng-controller="RawCtrl">
  <div class="span10">
    <h3>Upload raw data</h3>

    <form destination="b/doRawDataLoad" returnPath="/" name="form" upload-form novalidate>
      <table style="width: 400px;">
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
          <td style="vertical-align: top" colspan="2">
            <button class="btn ctrlAdd" style="display: block;" ng-click="addControl()">
              <i class="icon-plus icon-gray"></i>Add Control
            </button>
          </td>
        </tr>

        <tr ng-repeat="item in items">
          <td>Control</td>
          <td><input name="control" type="text" ng-model="control" required /><button class="btn" style="position: relative;top: -5px;margin-left: 10px;" ng-click="remove($index)"><i class="icon-ban-circle icon-gray"></i></button></td>
        </tr>
        
        <tr><td>&nbsp;</td></tr>
        
      </table>
      <button type="button" class="btn btn-primary start" ng-disabled="form.$invalid || loading" ng-click="upload()">
        <i class="icon-upload icon-white"></i> <span>Start upload</span>
      </button>
      <img src="img/loader.gif" ng-show="loading"/>
      <span class="failure">{{loadError}}</span>
    </form>
  </div>
</div>