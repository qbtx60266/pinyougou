app.controller("itemController",function ($scope) {


    $scope.num=1;



    

    //存储用户选择的规格
    $scope.specificationItems={};

    //数量增减
    $scope.addNum=function(x){
        $scope.num+=x;
        if($scope.num<1){
            $scope.num = 1;
        }
    }
    //用户选择规格
    $scope.selectSpecification=function(key,value){
        if(!$scope.isSelected(key,value)){
            $scope.specificationItems[key]=value;
        }else{
            delete $scope.specificationItems[key];
        }
        searchSku();
    }

    //检验规格是否被选中
   $scope.isSelected=function(key,value){
        if($scope.specificationItems[key] == value){
            return true;
        }else{
            return false;
        }
   }

   
    //当前选择的SKU
    $scope.sku={};

    //加载默认sku
    $scope.loadSku=function(){
        $scope.sku=skuList[0];
        $scope.specificationItems=JSON.parse(JSON.stringify($scope.sku.spec));
    }


    //匹配两个对象是否相等
    matchObject=function(map1,map2){
        for(var k in map1){
            if(map1[k] != map2[k]){
                return false;
            }
        }

        for(var k in map2){
            if(map1[k] != map2[k]){
                return false;
            }
        }
        return true;
    }


    //根据规格查找SKU
    searchSku=function(){
        for(var i = 0; i < skuList.length;i ++){
            if(matchObject(skuList[i].spec,$scope.specificationItems)){
                $scope.sku=skuList[i];
                return;
            }
        }
        $scope.sku={id:0,title:'--------------',price:'0'};
    }

    //添加商品到购物车
    $scope.addToCart=function(){
        alert('SKUID'+$scope.sku.id);
    }



})