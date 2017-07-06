(function () {
    'use strict';
    angular
        .module('quickMon')
        .factory('TraversonService', TraversonService);


    /**
     * This is a wrapper service around traverson. Reduces the duplicated config code
     * for a traverson object
     *
     */
    function TraversonService(traverson) {

        return {
            request: _request
        };

        /**
         * Return a configured traverson object that can be used in other services.
         * XMLHttpRequest is need in order for Spring to return a 403 when an Ajax request is made when
         * a user is not logged in.
         * @param url of REST API
         * @private
         */
        function _request(url) {
            traverson.registerMediaType(TraversonJsonHalAdapter.mediaType, TraversonJsonHalAdapter);
            return traverson.from(url).jsonHal().withRequestOptions({
                headers: {
                    'accept': 'application/hal+json',
                    'X-Requested-With': 'XMLHttpRequest'
                }
            });
        }
    }
})();

