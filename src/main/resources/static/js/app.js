(function () {

	var quickMon = angular.module('quickMon', [
		'ui.router',
//		'ui.bootstrap',
		'traverson'
	]);

    quickMon.constant('URL_QUICKMON_API', '/api/');


	quickMon.config( function ($locationProvider, $stateProvider, $urlRouterProvider) {
		
		$locationProvider.html5Mode(true);

		/*********** ROUTES ***********/

        $urlRouterProvider.when('/', '/ui/healthChecks');
		$urlRouterProvider.when('/ui', '/ui/healthChecks');
		
		$stateProvider
			.state('healthChecks', {
                url 			: '/ui/healthChecks?tags&status&columns&display&luceneQuery',
				templateUrl 	: 'js/healthChecks/healthChecks-list-template.html',
				controller 		: 'HealthChecksListController',
				controllerAs	: 'vm'
			})
			.state('healthChecksDetail', {
                url 			: '/ui/healthChecks/:healthCheckName',
                templateUrl 	: 'js/healthChecks/healthChecks-detail-template.html',
                controller 		: 'HealthChecksDetailController',
                controllerAs	: 'vm',
				resolve: HealthChecksDetailController.resolve

			});
	});

})();