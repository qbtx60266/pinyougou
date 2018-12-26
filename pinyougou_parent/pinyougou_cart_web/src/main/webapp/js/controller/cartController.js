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


    //获取当前用户的地址列表
    $scope.findAddressList=function () {
        cartService.findAddressList().success(function (response) {
            $scope.addressList=response;
            for (var i = 0; i < $scope.addressList.length; i ++){
                if ($scope.addressList[i].isDefault == '1'){
                    $scope.address = $scope.addressList[i];
                    break;
                }
            }
        })
    }

    //选择地址
    $scope.selectAddress=function (address) {
        $scope.address=address;
    }

    //判断地址对象是否当前选择地址
    $scope.isSelectedAddress=function (address) {
        if (address == $scope.address){
            return true;
        }else {
            return false;
        }
    }

    //新增收货地址
    $scope.addAddress=function () {
        cartService.addAddress($scope.entity).success(function (response) {
            if (response.success){
                $scope.findCartList();
            }else {
                alert(response.message);
            }
            
        })
    }

    //订单对象
    $scope.order={paymentType:'1'};
    //选择支付类型
    $scope.selectPayType=function (type) {
        $scope.order.paymentType=type;
    }

    //提交订单
    $scope.submitOrder=function () {
        //地址
        $scope.order.receiverAreaName=$scope.address.address;
        //手机号
        $scope.order.receiverMobile=$scope.address.mobile;
        //联系人
        $scope.order.receiver=$scope.address.contact;
        cartService.submitOrder($scope.order).success(function (response) {
            //alert(response.message);
            if (response.success){
                //页面跳转
                //如果微信支付,付款页面
                if ($scope.order.paymentType=='1'){
                    location.href="pay.html";
                }else {
                    //货到付款，提示页面
                    location.href="paysuccess.html";
                }
            }else {
                alert(response.message);
            }
        })
    }


});