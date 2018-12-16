app.service("contentService",function ($http) {
    //根据广告分类查询
    this.findByCategoryId=function (categoryId) {
        return $http.get("content/findByCategoryId.do?categoryId=" +categoryId );
    }



})