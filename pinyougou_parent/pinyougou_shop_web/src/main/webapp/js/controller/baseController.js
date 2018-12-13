app.controller("baseController",function ($scope) {
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



    //提取json中的某个属性并显示
    $scope.jsonToString=function (jsonString,key) {
        var json=JSON.parse(jsonString);
        var value="";
        for (var i = 0 ;i<json.length;i++){
            if (i > 0){
                value +=","
            }
            value += json[i][key];
        }
        return value;
    };


    //判断list集合中某个属性是否存在
    $scope.searchObjectByKey=function (list,key,keyValue) {
        for (var i = 0; i < list.length; i ++){
            if (list[i][key] == keyValue){
                return list[i];
            }
        }
        return null;
    }




});