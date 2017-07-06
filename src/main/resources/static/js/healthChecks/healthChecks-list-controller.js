angular.module('quickMon').controller('HealthChecksListController', HealthChecksListController);

function HealthChecksListController ($scope, $state, $stateParams, $interval, HealthChecks) {

	var vm = this;

	vm.query = $stateParams.luceneQuery ? $stateParams.luceneQuery : "*";

	vm.queryForm = vm.query;

	vm.selectedStatus = $stateParams.status;
	vm.columns = $stateParams.columns;
    vm.display = $stateParams.display;

	vm.healthChecks = [];
	vm.statuses = {};
	vm.tags = {};

	var refreshData = function() {
	     require(['lucene-query-parser.js'], function (lucenequeryparser) {
                // Use the Lucene Query Parser library here

                var luceneQuery = "" != vm.query ? lucenequeryparser.parse(vm.query) : {};
                HealthChecks.list($stateParams.tags, $stateParams.status, luceneQuery).then(function(result) {
                     vm.healthChecks = result;
                     HealthChecks.countStatuses($stateParams.tags, $stateParams.status, luceneQuery).then(function(result) {
                         vm.statuses = result;
                     });
                     HealthChecks.countTags($stateParams.tags, $stateParams.status, luceneQuery).then(function(result) {
                         vm.tags = result;
                     })
                 }).catch(function(error){
                     console.error("Unable to refresh healthchecks", error.message);
                 });
            });


    }

    refreshData();


    var promise = $interval(refreshData, 3000);

    $scope.$on('$destroy', function(){
        if (angular.isDefined(promise)) {
            $interval.cancel(promise);
            promise = undefined;
        }
    });

	vm.computeClass = function (healthCheck) {
	    var dynamicClass = 'card-info';

	    switch(healthCheck.lastHealth) {
            case 'OK':
                dynamicClass = 'card-success';
                break;
            case 'WARNING':
                dynamicClass = 'card-warning';
                break;
            case 'CRITICAL':
                dynamicClass = 'card-danger';
                break;
            default:

        }
	    return 'card card-inverse ' + dynamicClass;
    }

    vm.getColumnCount = function() {
	    if ($stateParams.columns) {
	        if ((vm.healthChecks.length / 3) < $stateParams.columns) {
	            return vm.healthChecks.length / 3;
            } else {
                return $stateParams.columns;
            }
        } else {
	        return 5;
        }
    }

    vm.display = function() {
	    if ($stateParams.display) {
	        return $stateParams.display;
        } else {
	        return "compact";
        }
    }

    vm.changeDisplay = function (display) {
	    switch (display) {
            case "compact":
                vm.columns=5;
                break;
            case "full":
                vm.columns=2;
                break;
            case "minimalistic":
                vm.columns=40;
                break;
        }
        $state.go('healthChecks', {luceneQuery: vm.query, display: display, columns: vm.columns}, {notify: true});
    }

    vm.search = function() {
	    if ('' === vm.queryForm || !vm.queryForm) {
	        vm.queryForm = '*';
        }
        vm.query = vm.queryForm;
	    refreshData();
        $state.go('healthChecks', {luceneQuery: vm.query}, {notify: false});
    }

    vm.addStatus = function(status) {
        if ('' === vm.queryForm || !vm.queryForm || vm.queryForm == '*') {
            vm.queryForm = "lastStatus:" + status;
        } else {
            vm.queryForm = '(' + vm.queryForm + ") AND lastStatus:" + status;
        }

	    vm.search();
    }

    vm.addTag = function(tag) {
        if ('' === vm.queryForm || !vm.queryForm || vm.queryForm == '*') {
            vm.queryForm = "tags:\"" + tag + "\"";
        } else {
            vm.queryForm = '(' + vm.queryForm + ") AND tags:\"" + tag + "\"";
        }

        vm.search();
    }

}
