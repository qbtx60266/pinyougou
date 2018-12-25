app.controller('cartController',function($scope,cartService){
//查询购物车列表
    $scope.findCartList=function(){
        cartService.findCartList().success(
            function(response){
                $scope.cartList=response;
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


});