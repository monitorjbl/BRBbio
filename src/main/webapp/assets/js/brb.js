//adding helper methods
if (typeof String.prototype.endsWith != 'function') {
  String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
  };
}
if (typeof String.prototype.startsWith != 'function') {
  String.prototype.startsWith = function(str) {
    return this.slice(0, str.length) == str;
  };
}

var app = angular.module('bio', []).config(function($routeProvider) {
  $routeProvider.when('/', {
    templateUrl : 'home.html'
  }).when('/help', {
    templateUrl : 'help.html'
  }).when('/rawUpload', {
    templateUrl : 'b/rawUpload'
  }).when('/admin', {
    templateUrl : 'b/auth/adminUi'
  }).when('/profile', {
    templateUrl : 'b/auth/profileUi'
  }).when('/createAccount', {
    templateUrl : 'b/auth/createUi'
  }).when('/linkedUpload', {
    templateUrl : 'b/linkedUpload'
  }).when('/independentUpload', {
    templateUrl : 'b/independentUpload'
  }).when('/deleteRun', {
    templateUrl : 'b/deleteRun'
  }).when('/viewNormalizedData', {
    templateUrl : 'b/viewNormalizedData'
  }).when('/viewZFactor', {
    templateUrl : 'b/viewZFactor'
  }).when('/viewViability', {
    templateUrl : 'b/viewViability'
  }).otherwise({
    redirectTo : '/'
  });
}).run(function($rootScope, $templateCache, $location) {
  $rootScope.$on('$viewContentLoaded', function() {
    $templateCache.removeAll();
  });

  $rootScope.$on('$routeChangeStart', function(next, current) {
    $rootScope.location = $location.$$path;
  });
});

app.directive('uploadForm', function factory($location) {
  return function preLink($scope, iElement, iAttrs) {
    $scope.uploadForm = function() {
      var formData = new FormData($(iElement).closest('form')[0]);
      $scope.loading = true;
      $scope.loadError = '';
      $.ajax({
        url : $(iElement).attr('destination'),
        type : 'POST',
        data : formData,
        cache : false,
        contentType : false,
        processData : false,
        success : function(data) {
          $scope.loading = false;
          $location.path($(iElement).attr('returnPath'));
          $scope.$apply();
        },
        error : function(data) {
          console.log(data);
          var err = JSON.parse(data.responseText);
          $scope.loading = false;
          $scope.loadError = 'Failed to upload. '+err.message;
          $scope.$apply();
        }
      });
    };
  };
});

app.factory('authRequestInterceptor', function($q, $location, $rootScope) {
  return {
    response : function(response) {
      if (response.config.url.startsWith('b/') && !response.headers().logged_in) {
        $rootScope.$broadcast('loggedOut');
        $location.path('/');
      }
      return response || $q.when(response);
    }
  };
});

app.config(function($httpProvider) {
  $httpProvider.interceptors.push('authRequestInterceptor');
});