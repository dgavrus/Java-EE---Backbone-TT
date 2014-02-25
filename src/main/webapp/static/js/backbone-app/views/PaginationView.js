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
        var current = this.paginationParams.attributes.activePage;
        var pagesForView = this.paginationParams.attributes.pagesForView;
        var pagesCount = this.paginationParams.attributes.pagesCount;
        var pages;
        sp = Math.max(current - Math.floor(pagesForView / 2), 1);
        lp = Math.min(sp + pagesForView - 1, pagesCount);
        if(lp == pagesCount){
            sp = lp - (pagesForView - 1);
        }
        if(parseInt(current) > lp && lp < parseInt(pagesCount)){
            sp += current - lp, lp = current;
        }
        pages = {sp: sp, lp: lp};
        return $.parseJSON(JSON.stringify(pages));
    },

    addPaginationAttributes:function(){
        var sp, lp;
        var current = this.paginationParams.attributes.activePage;
        var pagesForView = this.paginationParams.attributes.pagesForView;
        var pagesCount = this.paginationParams.attributes.pagesCount;
        var pageName;
        if(this.paginationParams.attributes.url === "/rest/transaction/pagination"){
            pageName = "transactions";
        } else if(this.paginationParams.attributes.url === "/rest/userslist/pagination"){
            pageName = "accounts";
        }
        var hrefPrefix = "#" + pageName + "/page/";
        sp = Math.max(current - Math.floor(pagesForView / 2), 1);
        lp = Math.min(sp + pagesForView - 1, pagesCount);
        if(lp == pagesCount){
            sp = lp - (pagesForView - 1);
        }
        for(var pageNumber = sp; pageNumber <= lp; pageNumber++){
            console.log($("#aPage" + pageNumber));
            $("#aPage" + pageNumber).attr("href", hrefPrefix + pageNumber);
        }
        $("#liPage" + current).addClass("active");
        $("#liPrev").addClass(current <= 1 ? "disabled" : "default");
        $("#aPrev").attr("href", hrefPrefix + (current <= 1 ? 1 : current - 1));
        $("#liNext").addClass(current >= pagesCount ? "disabled" : "default");
        $("#aNext").attr("href", hrefPrefix + (current >= pagesCount ? pagesCount : parseInt(current) + 1));
    }

});