app.controller("searchController",function ($scope,searchService) {

    //定义搜索对象的结构
    $scope.searchMap={'keyword':'','category':'','brand':'','spec':{}};



    //搜索
    $scope.search=function () {
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap=response;
        })
    }
})