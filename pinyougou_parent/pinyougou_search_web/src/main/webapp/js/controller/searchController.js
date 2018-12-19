app.controller("searchController",function ($scope,searchService) {

    //定义搜索对象的结构
    $scope.searchMap={'keyword':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40};



    //搜索
    $scope.search=function () {
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap=response;
            buildPageLabel();
        })
    }

    //构建分页栏
    buildPageLabel=function () {
        //构建分页栏
        $scope.pageLabel=[];
        var firstPage=1;
        var lastPage=$scope.resultMap.totalPages;

        //总页数大于7
        if ($scope.resultMap.totalPages > 7){
            if ($scope.searchMap.pageNo <= 4){
                lastPage = 7;
            }else if ($scope.searchMap.pageNo > $scope.resultMap.totalPages - 3){
                firstPage = $scope.resultMap.totalPages - 6;
            }else {
                firstPage = $scope.searchMap.pageNo - 3;
                lastPage = $scope.searchMap.pageNo + 3;
            }
        }

        for (var i = firstPage; i <= lastPage; i ++){
            $scope.pageLabel.push(i);
        }
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