angular.module('quickMon').factory('HealthChecks', HealthChecksService);

function HealthChecksService(TraversonService, URL_QUICKMON_API) {

    return {
        list: list,
        countTags: countTags,
        countStatuses: countStatuses,
        detail: detail
    };

    function detail(name) {
        var promise = TraversonService
            .request(URL_QUICKMON_API)
            .follow('healthChecks', 'healthCheck')
            .withTemplateParameters( {name: name})
            .getResource()
            .result;

        return promise.then(function(document) {
            return document;
        });
    }

    function list(tags, status, query) {
        var promise = TraversonService
            .request(URL_QUICKMON_API)
            .follow('healthChecks', 'search')
            .withTemplateParameters( {tags: tags, status: status, luceneQuery: JSON.stringify(query)})
            .getResource()
            .result;

        return promise.then(function(document) {
            if (document._embedded) {
                return document._embedded.lightweightHealthCheckList;
            } else {
                return {};
            }
        });
    }

    function countTags(tags, status, query) {
        var promise = TraversonService
            .request(URL_QUICKMON_API)
            .follow('healthChecks', 'searchTags')
            .withTemplateParameters( {tags: tags, status: status, luceneQuery:JSON.stringify(query)})
            .getResource()
            .result;

        return promise.then(function(document) {
            return document.content;
        });
    }

    function countStatuses(tags, status, query) {
        var promise = TraversonService
            .request(URL_QUICKMON_API)
            .follow('healthChecks', 'searchStatuses')
            .withTemplateParameters( {tags: tags, status: status, luceneQuery:JSON.stringify(query)})
            .getResource()
            .result;

        return promise.then(function(document) {
            return document.content;
        });
    }



}