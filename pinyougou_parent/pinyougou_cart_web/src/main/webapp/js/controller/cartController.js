app.controller('cartController',function($scope,cartService){

    $scope.totalValue={totalNum:0,totalMoney:0};


//查询购物车列表
    $scope.findCartList=function(){
        cartService.findCartList().success(
            function(response){
                $scope.cartList=response;
                $scope.totalValue=cartService.sum($scope.cartList);
            }
        );
    }

    //购物车数量加减
    $scope.addGoodsToCartList=function (itemId,num) {
        cartService.addGoodsToCartList(itemId,num).success(function (response) {
            if (response.success){
                $scope.findCartList();
            }else {
                alert(response.message);
            }
        })
    }
    // //合计
    // sum=function () {
    //     //总数量
    //     $scope.totalNum=0;
    //     //总金额
    //     $scope.totalFee=0;
    //     for (var i = 0 ; i < $scope.cartList.length; i ++){
    //         //购物车对象
    //         var cart = $scope.cartList[i];
    //         for (var j = 0 ; j < cart.orderItemList.length; j ++){
    //             //购物车明细
    //             var orderItem=  cart.orderItemList[i]
    //             //累加数量
    //             $scope.totalNum+=orderItem.num;
    //             //累加金额
    //             $scope.totalMoney+=orderItem.totalFee;
    //         }
    //     }
    // }


});