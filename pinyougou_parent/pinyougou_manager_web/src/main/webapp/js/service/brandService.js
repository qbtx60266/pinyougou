//品牌服务
app.service("brandService",function ($http) {
    this.findAll=function () {
        return $http.post("../brand/findAll.do");
    };

    this.findPage=function (page,rows) {
        return $http.get("../brand/findPage.do?page="+page+"&rows=" + rows);
    };


    this.findOne=function (id) {
        return $http.get("../brand/findOne.do?id=" + id);
    };

    this.save=function (entity) {
        return $http.post("../brand/save.do",entity);
    };

    this.dele=function (ids) {
        return $http.get("../brand/delete.do?ids="+ids);
    };


    this.search=function (page,rows,entity) {
        return $http.post("../brand/search.do?page="+page+"&rows="+rows,entity);
    }

    //下拉列表数据
    this.selectOptionList=function () {
        return $http.get("../brand/selectOptionList.do");
    }
});