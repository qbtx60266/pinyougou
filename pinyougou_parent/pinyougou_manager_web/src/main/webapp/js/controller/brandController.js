//品牌控制器
app.controller("brandController",function ($scope,$http,brandService) {
    $scope.findAll=function () {
        brandService.findAll().success(function (data) {
            $scope.list=data;
        });
    };
    //分页控件配置
    $scope.paginationConf={
        currentPage:1,//当前页
        totalItems:10,//总记录数
        itemsPerPage:10,//每页记录数
        perPageOptions:[10,20,30,40,50],//分页选项
        onChange:function () {//当页码变更后自动触发的方法
            $scope.reloadList();
        }
    };
    //页码变更操作
    $scope.reloadList=function () {
        // $scope.findPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);//重新加载
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);//重新加载
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

    $scope.selectIds=[];//选中的集合id

    //将选中id提交到集合
    $scope.updateSelection=function ($event,id) {
        if ($event.target.checked){
            $scope.selectIds.push(id);
        }else {
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index,1);
        }
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