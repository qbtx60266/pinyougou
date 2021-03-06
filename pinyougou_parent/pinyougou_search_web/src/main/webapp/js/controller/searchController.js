app.controller("searchController",function ($scope,searchService,$location) {

    //定义搜索对象的结构
    $scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};



    //搜索
    $scope.search=function () {
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);
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


    //分页查询
    $scope.queryByPage = function(pageNo){
        if (pageNo < 1 || pageNo > $scope.resultMap.totalPages){
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search();
    }

    //排序查询
    $scope.sortSearch=function (sortField,sort) {
        $scope.searchMap.sortField=sortField;
        $scope.searchMap.sort=sort;
        $scope.search();

    }

    //关键字判断
    $scope.keywordsIsBrand=function () {
        for (var i = 0; i < $scope.resultMap.brandList.length;i ++){
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text) >= 0){
                return true;
            }
        }
        return false;
    }


    //接收参数进行查询
    $scope.loadKeywords=function () {
        $scope.searchMap.keywords =  $location.search()['keywords'];
        $scope.search();
    }
})