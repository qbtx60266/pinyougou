//品牌控制器
app.controller("brandController",function ($scope,$controller,brandService) {


    $controller('baseController',{$scope:$scope});





    $scope.findAll=function () {
        brandService.findAll().success(function (data) {
            $scope.list=data;
        });
    };

    //分页查询
    $scope.findPage=function (page,rows) {
        brandService.findPage(page,rows).success(
            function (data) {
                $scope.list = data.rows;//当前页数据
                $scope.paginationConf.totalItems=data.total;//更新总记录

            }
        )
    };
    //新增方法
    $scope.save=function () {
        brandService.save($scope.entity).success(
            function (data) {
                if(data.success){
                    $scope.reloadList();
                }else {
                    alert(data.message);
                }
            }
        )
    };

    //查询实体
    $scope.findOne=function (id) {
        brandService.findOne(id).success(
            function (data) {
                $scope.entity=data;
            }
        )
    };


    //删除
    $scope.dele=function () {
        brandService.dele($scope.selectIds).success(
            function (data) {
                if (data.success){
                    $scope.reloadList();
                }else {
                    alert(data.message);
                }
                $scope.selectIds=[];
            }
        )
    };


    $scope.searchEntity={};//定义搜索对象

    $scope.search=function (page, rows) {
        brandService.search(page,rows,$scope.searchEntity).success(
            function (data) {
                $scope.paginationConf.totalItems=data.total;//总记录数
                $scope.list=data.rows;//给列表变量赋值
            }
        )
    }


});