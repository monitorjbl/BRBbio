app.controller('Main', function ($scope, $rootScope, $http, $location) {
  $scope.init = false;
  $scope.getUserDetails = function () {
    $http({
      url: 'b/auth/getInfo',
      method: 'GET'
    }).success(function (response) {
      $scope.init = true;
      $scope.auth = response;
    }).error(function (response) {
      $scope.auth = undefined;
      $scope.init = true;
    });
  };
  $scope.getUserDetails();

  $rootScope.$on('loggedOut', function (event, e) {
    $scope.auth = undefined;
  });

  $rootScope.$on('loggedIn', function (event, e) {
    console.log('login');
    $scope.getUserDetails();
  });

  $scope.login = function () {
    $scope.error = '';
    $http({
      url: 'j_spring_security_check',
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      data: $('form.loginForm').serialize()
    }).success(function (response) {
      $scope.auth = response;
    }).error(function (response) {
      $scope.error = 'Unable to login';
    });
  };

});

app.controller('Nav', function ($scope, $rootScope, $http, $location) {

});

app.controller('PasswordUpdate', function ($scope, $http, $location, $element) {
  $scope.invalid = function () {
    return $scope.newPassword == '' || $scope.newPassword != $scope.repeatPassword;
  };
  $scope.updatePassword = function () {
    $http({
      url: 'b/auth/updatePassword',
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      data: $element.children('form').serialize()
    }).success(function (response) {
      $location.path('/');
    }).error(function (response) {
      $scope.error = response.message;
    });
  };
});

app.controller('UserCreate', function ($scope, $rootScope, $location, $http, $element) {
  $scope.invalid = function () {
    return $scope.rptPassword == '' || $scope.password != $scope.rptPassword;
  };
  $scope.updatePassword = function () {
    $http({
      url: 'b/auth/createUser',
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      data: $element.children('form').serialize()
    }).success(function (response) {
      $rootScope.$broadcast('loggedIn');
      $location.path('/');
    }).error(function (response) {
    });
  };
});

app.controller('RawCtrl', function ($scope, $location, $http) {
  $scope.updateFilename = function (ele) {
    $scope.filename = ele.value.substring(ele.value.lastIndexOf('\\') + 1);
    $scope.$apply();
  };

  $scope.controls = [];
  $scope.addControl = function () {
    $scope.controls.push({
      val: ''
    });
  };
  $scope.remove = function (item) {
    var index = $scope.controls.indexOf(item);
    $scope.controls.splice(index, 1);
  };

  $scope.getRun = function (id) {
    $http.get('b/getRawDataControlsForRun', {
      params: {
        runId: id
      }
    }).success(function (data) {
      $scope.controls = [];
      angular.forEach(data, function (val) {
        $scope.controls.push({
          val: val
        });
      });
    });
  };

  $scope.upload = function () {
    $scope.uploadForm();
  };

  $scope.deleteRun = function () {
    if (confirm('Are you sure you want to delete this run? This cannot be undone.')) {
      $http.get('b/deleteRunById', {
        params: {
          runId: $scope.run
        }
      }).success(function () {
        $location.path('/');
        // $scope.$apply();
      });
    }
  };
});

app.controller('HomologueController', function ($scope, $location, $http, $filter) {
  var getSpecies = function () {
    $http.get('b/ncbi/getTaxonomies').success(function (data) {
      $scope.species = [
        {id: -1, name: '[-Select-]'}
      ];
      $scope.species = $scope.species.concat(data);
      $scope.taxonomyId = -1;
    }).error(function (data) {

    });
  };
  var getRuns = function () {
    $http.get('b/getRuns?includeViability=false').success(function (data) {
      $scope.runs = [
        {id: -1, runName: '[-Select-]'}
      ];
      $scope.runs = $scope.runs.concat(data);
      $scope.runId = -1;
    }).error(function (data) {

    });
  };
  var getLastUpdatedTime = function () {
    $http.get('b/ncbi/lastLoadTime').success(function (data) {
      if (data == '') {
        $scope.lastUpdated = "Updating now...";
      } else {
        $scope.lastUpdated = $filter('date')(data,'MM-dd-yyyy hh:mm:ss');
      }
    }).error(function (data) {

    });
  }
  $scope.getHomologues = function (runId, taxonomyId) {
    $scope.loading = true;
    $http.get('b/ncbi/getHomologue', {
      params: {
        runId: runId,
        taxonomyId: taxonomyId
      }
    }).success(function (data) {
      $scope.rows = data;

      $scope.showingData = true;
      $scope.loading = false;
      $scope.loaded = true;
    }).error(function (data) {
      $scope.loading = false;
      $scope.loadError = 'Failed to retrieve data :(';
    });
  };


  getSpecies();
  getRuns();
  getLastUpdatedTime();
});

app.controller('DisplayController', function ($scope, $location, $http) {
  $scope.hours = [];
  $scope.rows = [];
  $scope.controls = [];
  $scope.showingData = false;
  $scope.loading = false;
  $scope.loaded = false;
  $scope.run = -1;

  $scope.encodeUrl = function (str) {
    return encodeURIComponent(str);
  };

  $scope.getRawControls = function () {
    $scope.loaded = false;
    $scope.loadError = '';
    $http.get('b/getRawDataControlsForRun', {
      params: {
        runId: $scope.run
      }
    }).success(function (data) {
      $scope.controls = data;
      $scope.showingData = false;
      $scope.hours = [];
      $scope.rows = [];
      if ($scope.controls.length == 0) {
        $scope.loadError = 'No controls found';
      }
    });
  };

  $scope.getViabilityControls = function () {
    $scope.loaded = false;
    $scope.loadError = '';
    $http.get('b/getViabilityControlsForRun', {
      params: {
        runId: $scope.run
      }
    }).success(function (data) {
      $scope.controls = data;
      $scope.showingData = false;
      $scope.hours = [];
      $scope.rows = [];
      if ($scope.controls.length == 0) {
        $scope.loadError = 'No controls found';
      }
    });
  };


  $scope.getNormalizedData = function () {
    $scope.loadError = '';
    $scope.showingData = false;
    $scope.loading = true;
    $scope.hours = [];
    $scope.rows = [];

    $http.get('b/getNormalizedData', {
      params: {
        runId: $scope.run,
        func: $scope.func
      }
    }).success(function (data) {
      var time = {};
      var tableRows = {};

      $.each(data, function () {
        var key = this.plateName + '_' + this.geneId;
        if (tableRows[key] == undefined) {
          tableRows[key] = {
            plate: this.plateName,
            geneId: this.geneId,
            geneSymbol: this.geneSymbol,
            data: []
          };
          $scope.rows.push(tableRows[key]);
        }
        tableRows[key].data.push(this.normalized);
        time['_' + this.timeMarker] = true;
      });

      for (key in time) {
        $scope.hours.push(key.substring(1) + 'hr');
      }

      $scope.showingData = true;
      $scope.loading = false;
      $scope.loaded = true;
    }).error(function (data) {
      $scope.loading = false;
      $scope.loadError = 'Failed to retrieve data :(';
    });
  };

  $scope.getZFactor = function () {
    $scope.loadError = '';
    $scope.showingData = false;
    $scope.loading = true;
    $scope.hours = [];
    $scope.rows = [];

    $http.get('b/getZFactorData', {
      params: {
        runId: $scope.run,
        func: $scope.func
      }
    }).success(function (data) {
      var time = {};
      var tableRows = {};

      $.each(data, function () {
        var key = this.plateName + '_' + this.geneId;
        if (tableRows[key] == undefined) {
          tableRows[key] = {
            plate: this.plateName,
            data: []
          };
          $scope.rows.push(tableRows[key]);
        }
        tableRows[key].data.push(this.zFactor);
        time['_' + this.timeMarker] = true;
      });

      for (key in time) {
        $scope.hours.push(key.substring(1) + 'hr');
      }

      $scope.showingData = true;
      $scope.loading = false;
      $scope.loaded = true;

      var d = [];
      var plates = [];
      var count = 0;
      $.each(tableRows, function () {
        plates.push(this.plate);
        $.each(this.data, function (i, j) {
          if (i >= d.length) {
            d.push([]);
          }
          d[i].push([ count, j ]);
        });
        count++;
      });

      var series = [];
      $.each($scope.hours, function (i, j) {
        series.push({
          name: j,
          data: d[i]
        });
      });

      $('#chart').highcharts({
        chart: {
          type: 'scatter',
          zoomType: 'xy',
          events: {
            load: function (event) {
              setTimeout(function () {
                $('#chart').highcharts().setSize($('#chart').width(), $('#chart').height());
              }, 150);
            }
          }
        },
        turboThreshold: 0,
        title: {
          text: 'ZFactors by plate and time'
        },
        subtitle: {
          text: 'Source'
        },
        xAxis: {
          title: {
            enabled: true,
            text: 'Plate'
          },
          format: {
            type: "category"
          },
          categories: plates,
          labels: {
            rotation: -45,
            align: 'right',
            style: {
              fontSize: '9px',
              fontFamily: 'Verdana, sans-serif'
            }
          }
        },
        yAxis: {
          title: {
            text: 'ZFactor'
          },
          max: 1,
          min: 0
        },
        legend: {
          layout: 'vertical',
          align: 'left',
          verticalAlign: 'top',
          x: 100,
          y: 70,
          floating: true,
          backgroundColor: '#FFFFFF',
          borderWidth: 1
        },
        plotOptions: {
          scatter: {
            marker: {
              radius: 5,
              states: {
                hover: {
                  enabled: true,
                  lineColor: 'rgb(100,100,100)'
                }
              }
            },
            states: {
              hover: {
                marker: {
                  enabled: false
                }
              }
            },
            tooltip: {
              headerFormat: '<b>{series.name}</b><br>',
              pointFormat: '{point.category}, {point.y}'
            }
          }
        },

        series: series
      });
    }).error(function (data) {
      $scope.loading = false;
      $scope.loadError = 'Failed to retrieve data :(';
    });
  };

  $scope.getViabilityData = function () {
    $scope.loadError = '';
    $scope.showingData = false;
    $scope.loading = true;
    $scope.hours = [];
    $scope.rows = [];

    $http.get('b/getViabilityData', {
      params: {
        runId: $scope.run,
        func: $scope.func
      }
    }).success(function (data) {
      $.each(data, function () {
        $scope.rows.push({
          plate: this.plateName,
          geneId: this.geneId,
          geneSymbol: this.geneSymbol,
          data: this.normalized
        });
      });

      $scope.showingData = true;
      $scope.loading = false;
      $scope.loaded = true;
    }).error(function (data) {
      $scope.loading = false;
      $scope.loadError = 'Failed to retrieve data :(';
    });
  };
});