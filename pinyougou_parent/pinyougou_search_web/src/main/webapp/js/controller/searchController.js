app.controller("searchController",function ($scope,searchService) {

    //定义搜索对象的结构
    $scope.searchMap={'keyword':'','category':'','brand':'','spec':{},'price':''};



    //搜索
    $scope.search=function () {
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap=response;
        })
    }

    //添加搜索项方法
    $scope.addSearchItem=function (key, value) {
        //点击分类或品牌
        if (key == 'category' || key == 'brand' || key == 'price'){
            $scope.searchMap[key]=value;
        }else {
            //点击规格
            $scope.searchMap.spec[key] = value;

        }
        //查询
        $scope.search();
    }

    //撤销搜索项方法
    $scope.removeSearchItem=function (key) {
        //点击分类或品牌
        if (key == 'category' || key == 'brand' || key == 'price'){
            $scope.searchMap[key]='';
        }else {
            //点击规格
            delete $scope.searchMap.spec[key];

        }
        //查询
        $scope.search();
    }
})