 //控制层 
app.controller('goodsController' ,function($scope,$controller,itemCatService,goodsService,$location,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    //查询实体
    $scope.findOne=function(){
        var id = $location.search()['id'];
        if (id == null){
            return;
        }else {
            goodsService.findOne(id).success(
                function (response) {
                    $scope.entity = response;
                    //商品介绍
                    editor.html($scope.entity.goodsDesc.introduction);
                    //商品图片
                    $scope.entity.goodsDesc.itemImages=JSON.parse($scope.entity.goodsDesc.itemImages);
                    //扩展属性
                    $scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                    //规格选择
                    $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
                    //转换SKU列表中的规格对象
                    for (var i = 0;i < $scope.entity.itemList.length;i ++){
                        $scope.entity.itemList[i].spec=JSON.parse($scope.entity.itemList[i].spec);
                    }
                }
            );
        }
    }
	
	// 保存
	$scope.save=function(){
		var serviceObject;//服务层对象
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加
		}
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}
		);
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	//初始化对象
    $scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};

    $scope.status=['未审核','已审核','审核未通过','已关闭'];
    //商品分类列表
    $scope.itemCatList=[];

    $scope.findItemCatList=function () {
        itemCatService.findAll().success(function (response) {
            for (var i = 0; i <response.length;i ++){
                $scope.itemCatList[response[i].id]=response[i].name;
            }
        })
    }


    //批量修改状态
	$scope.updateStatus=function (status) {
		goodsService.updateStatus($scope.selectIds,status).success(function (response) {
			if (response.success){
				$scope.reloadList();
				$scope.selectIds=[];
			}else {
				alert(response.message);
			}
        })
    }


    //查询一级商品分类列表
    $scope.selectItemCat1List=function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.itemCat1List=response;
        })
    }
    $scope.tempCategory1=1;
    $scope.tempCategory2=1;
    //查询二级商品分类列表
    $scope.$watch('entity.goods.category1Id',function (newValue,oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            if ($location.search()['id']==null || $scope.tempCategory1 > 1){
                $scope.entity.goods.category2Id={};
                $scope.entity.goods.category3Id={};
            }
            $scope.tempCategory1 ++;
            $scope.itemCat2List=response;
        })
    });


    //查询三级商品分类列表
    $scope.$watch('entity.goods.category2Id',function (newValue,oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            if ($location.search()['id']==null || $scope.tempCategory2 > 1){
                $scope.entity.goods.category3Id={};
            }
            $scope.tempCategory2 ++;
            $scope.itemCat3List=response;
        })
    });

    //查询模板id
    // $scope.$watch('entity.goods.category3Id',function (newValue,oldValue) {
    //     itemCatService.findOne(newValue).success(function (response) {
    //         $scope.entity.goods.typeTemplateId=response.typeId;
    //     })
    // });


    //根据模板id确定品牌列表 扩展属性 规格列表
    $scope.$watch('entity.goods.typeTemplateId',function (newValue,oldValue) {
        typeTemplateService.findOne(newValue).success(function (response) {
            $scope.typeTemplate=response;//模板对象
            $scope.typeTemplate.brandIds=JSON.parse($scope.typeTemplate.brandIds);//品牌列表转换
            //如果增加商品
            if ($location.search()['id']==null){
                $scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.typeTemplate.customAttributeItems);//扩展信息转换
            }
        });
        typeTemplateService.findSpecList(newValue).success(function (response) {
            $scope.specList=response;
        });
    });

    //产生一个存储变量得集合
    $scope.updateSpecAttribute=function ($event,name,value) {
        var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,'attributeName',name);
        if (object != null){
            if ($event.target.checked){
                object.attributeValue.push(value);
            }else {
                object.attributeValue.splice(object.attributeValue.indexOf(object),1);
                if (object.attributeValue.length==0){
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object),1);
                }
            }
        }else {
            $scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
        }
    }

    //创建SKU列表
    $scope.createItemList=function () {
        //列表初始化
        $scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0'}];
        var items=$scope.entity.goodsDesc.specificationItems;
        for (var i = 0; i <items.length;i ++){
            $scope.entity.itemList=addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
        }
    };

    //增添列的方法
    addColumn=function (list,columnName,columnValues) {
        var newList=[];

        for (var i = 0 ; i < list.length;i ++){
            var oldRow = list[i];
            for (var j = 0;j < columnValues.length; j ++){
                //深克隆
                var newRow = JSON.parse(JSON.stringify(oldRow));
                newRow.spec[columnName] = columnValues[j];
                newList.push(newRow);
            }
        }

        return newList;
    }
    $scope.checkAttributeValue=function (specName,optionName) {
        var items = $scope.entity.goodsDesc.specificationItems;
        var object = $scope.searchObjectByKey(items,'attributeName',specName);
        if (object == null){
            return false;
        }else {
            if (object.attributeValue.indexOf(optionName) >= 0){
                return true;
            }else {
                return false;
            }
        }
    }
    
});	
