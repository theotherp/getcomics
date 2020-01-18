var craptureApp = angular.module('craptureApp', ['ui.router', 'angular-loading-bar', 'ngAnimate', 'ui.bootstrap', 'ipCookie', 'angular-growl', 'angular.filter', 'angular-growl', 'cgBusy', 'sticky', 'ui.grid', 'ui.grid.pagination', 'ui.grid.resizeColumns', 'ui.grid.selection', 'ui.grid.cellNav']);

angular
    .module('craptureApp').config(function ($stateProvider, $urlRouterProvider, $locationProvider, $urlMatcherFactoryProvider) {


});


'use strict';

var navbarControllerModule = angular
    .module('craptureApp');

navbarControllerModule.controller('GlobalController', ['$scope', '$http', '$location', 'ipCookie', '$state', function ($scope, $http, $location, ipCookie, $state) {

    $scope.page = {};
    $scope.posts = [];

    $scope.iniateSearch = function () {
        $http.get('search/' + $scope.query + "/" + 1,).then(function (response) {

            $scope.page = response.data;
            $scope.posts = $scope.page.posts;
        });
    };

    $scope.loadNextPage = function () {
        $http.get('search/' + $scope.query + "/" + ($scope.page.currentPage + 1),).then(function (response) {
            $scope.page = response.data;
            $scope.posts = $scope.posts.concat($scope.page.posts);
        });
    };

    $scope.download = function () {
        let selectedPosts = _.filter($scope.posts, function (post) {
            return post.checked
        });
        let selectedLinks = _.pluck(selectedPosts, 'link');
        console.log(selectedLinks);

        $http.put('download', selectedLinks).then(function (response) {

        });
    };

    $scope.query = "spawn";
    $scope.iniateSearch();


}]);
