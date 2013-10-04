<div class="span5" ng-controller="PasswordUpdate">
  <h3>Update password</h3>
  <form name="form" ng-submit="updatePassword()">
    <table class="table">
      <tr>
        <td>Old Password</td>
        <td><input name="oldPassword" type="text" required /></td>
      </tr>
      <tr>
        <td>New Password</td>
        <td><input type="password" required ng-model="newPassword" /></td>
      </tr>
      <tr>
        <td>Repeat New Password</td>
        <td><input name="newPassword" type="password" required ng-model="repeatPassword" /></td>
      </tr>
      <tr>
        <td><span class="btn btn-info" ng-disabled="form.$invalid || invalid()" ng-click="updatePassword()">Save</span></td>
        <td><span class="errorMessage">{{error}}</span></td>
      </tr>
    </table>
    <input type="submit" class="invisibleSubmit" />
  </form>
</div>