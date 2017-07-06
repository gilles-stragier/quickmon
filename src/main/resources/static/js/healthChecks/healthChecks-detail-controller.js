angular.module('quickMon').controller('HealthChecksDetailController', HealthChecksDetailController);

function HealthChecksDetailController ($scope, $stateParams, healthCheck) {
	
	var vm = this;
	vm.healthCheck = healthCheck;
	vm.currentTab = "overview"

    vm.computeStatusSpanClass = function(key) {
        var dynamicClass = 'list-group-item-info';

        switch(key) {
            case 'OK':
                dynamicClass = 'list-group-item-success';
                break;
            case 'WARNING':
                dynamicClass = 'list-group-item-warning';
                break;
            case 'CRITICAL':
                dynamicClass = 'list-group-item-danger';
                break;
            default:

        }
        return dynamicClass;
    }

    vm.switchTab = function(tab) {
	    vm.currentTab = tab;
    }

    vm.computeClass = function () {
        var dynamicClass = 'card-outline-primary';

        switch(vm.healthCheck.lastStatus.health) {
            case 'OK':
                dynamicClass = 'card-outline-success';
                break;
            case 'WARNING':
                dynamicClass = 'card-outline-warning';
                break;
            case 'CRITICAL':
                dynamicClass = 'card-outline-danger';
                break;
            default:

        }
        return dynamicClass;
    }



}

HealthChecksDetailController.resolve =	{
    healthCheck : function (HealthChecks, $stateParams) {
        return HealthChecks.detail($stateParams.healthCheckName);
    }
}
