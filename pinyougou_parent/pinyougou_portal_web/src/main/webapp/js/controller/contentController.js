app.controller("contentController",function ($scope, contentService) {
    //广告列表
    $scope.contentList=[];


    //根据广告分类查询广告
    $scope.findByCategoryId=function (categoryId) {
        contentService.findByCategoryId(categoryId).success(function (response) {
            $scope.contentList[categoryId]=response;
        });
    }
    
    
    //传递参数前往搜索页
    $scope.search=function () {
        location.href = "http://localhost:9104/search.html#?keywords=" + $scope.keywords;
    }


})