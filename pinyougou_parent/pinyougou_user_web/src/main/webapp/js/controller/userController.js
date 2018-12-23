 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){	


    $scope.entity={};

    //注册
    $scope.reg=function () {
        //比较两次输入的密码是否一致
        if ($scope.password != $scope.entity.password){
            alert("输入密码不一致，请重新输入");
            $scope.password = '';
            $scope.entity.password='';
            return;
        }else {
            userService.add($scope.entity,$scope.smsCode).success(function (response) {
                alert(response.message);
            })
        }

    }


    //发送验证码
    $scope.sendCode=function () {
        if ($scope.entity.phone == null || $scope.entity.phone == ""){
            alert("请填写手机号码");
            return;
        }
        userService.sendCode($scope.entity.phone).success(function (response) {
            alert(response.message);
        })
    }

});	
