window.PaginationView = Backbone.View.extend({

    el: "#paginationdiv",

    initialize: function(params){
        this.paginationParams = params.paginationParams;
        this.render();
    },

    render: function(){
        $(this.el).html(this.template());
    },

    paginationParams: null,

    getPages: function(){
        var sp, lp;
        var pages = "[";
        var current = this.paginationParams.attributes.activePage;
        var pagesForView = this.paginationParams.attributes.pagesForView;
        var pagesCount = this.paginationParams.attributes.pagesCount;
        var pageName;
        if(this.paginationParams.attributes.url === "/rest/transaction/pagination"){
            pageName = "transactions";
        } else if(this.paginationParams.attributes.url === "/rest/userslist/pagination"){
            pageName = "accounts";
        }
        sp = 1, lp = pagesForView;
        if(current > lp && lp < pagesCount){
            sp += current - lp, lp = current;
        }
        var liClass = current <= 1 ? "disabled" : "default";
        pages += "{\"pageName\":\"" + pageName + "\",\"liClass\":\"" + liClass + "\",\"pageNumberText\":\"prev\",\"pageNumber\":" + (current <= 1 ? current : (parseInt(current) - 1)) + "},";
        for(var i = sp; i <= lp; i++){
            liClass = current == i ? "active" : "default";
            pages += "{\"current\":" + current + ",\"pageName\":\"" + pageName + "\",\"liClass\":\"" + liClass + "\",\"pageNumberText\":" + i + ",\"pageNumber\":" + i +"},";
        }
        liClass = current >= pagesCount ? "disabled" : "default";
        pages += "{\"current\":" + current + ",\"pageName\":\"" + pageName + "\",\"liClass\":\"" + liClass + "\",\"pageNumberText\":\"next\",\"pageNumber\":" + (current >= pagesCount ? current : (parseInt(current) + 1)) + "}";
        pages += "]";
        return $.parseJSON(pages);
    }

});