<div class="span5" ng-controller="UserCreate">
  <h3>Create Account</h3>
  <form name="form" ng-submit="createUser()">
    <table class="table">
      <tr>
        <td>Username</td>
        <td><input name="username" type="text" required /></td>
      </tr>
      <tr>
        <td>First Name</td>
        <td><input name="firstName" type="text" required /></td>
      </tr>
      <tr>
        <td>Last Name</td>
        <td><input name="lastName" type="text" required /></td>
      </tr>
      <tr>
        <td>Password</td>
        <td><input type="password" required ng-model="password" /></td>
      </tr>
      <tr>
        <td>Repeat Password</td>
        <td><input name="password" type="password" required ng-model="rptPassword" /></td>
      </tr>
      <tr>
        <td><span class="btn btn-info" ng-disabled="form.$invalid || invalid()" ng-click="updatePassword()">Save</span></td>
        <td></td>
      </tr>
    </table>
    <input type="submit" class="invisibleSubmit" />
  </form>
</div>