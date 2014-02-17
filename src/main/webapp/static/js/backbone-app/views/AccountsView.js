window.AccountsView = Backbone.View.extend({

    initialize:function (params) {
        this.pagination = params.pagination;
        this.collection.on('sync', this.render, this);
        this.pagination.paginationParams.on('sync',this.render, this);
        loginStatus.on('sync', this.username, this);
        loginStatus.fetch();
        console.log('Initializing Accounts View');
    },

    render:function () {
        $(this.el).html(this.template());
        var template = $.templates("#template");
        template.link(this.$("#result"), this.collection.toJSON());
        $("#paginationdiv").html(this.pagination.template());
        var pageTemplate = $.templates("#pageTemplate");
        pageTemplate.link(this.$("#tpagination"), this.pagination.getPages());
        return this;

    },

    events: {
        "click .btn" : "showDetails"
    },

    /*changeStatus: function(e){
     e.preventDefault();
     var status = $(e.target).text();
     var accountId = e.target.parentNode.id;
     var currentAccount = this.collection.findWhere({accountNumber: parseInt(accountId)});
     currentAccount.set({status: status});
     currentAccount.save(null, {
     success: function(){
     this.collection.fetch();
     }});
     this.render();
     },*/

    showDetails: function(e){
        e.preventDefault();
        var accountId = e.target.parentNode.id;
        var self = this;
        var currentAccount = this.collection.findWhere({accountNumber: parseInt(accountId)});
        var accountDetailsView = new AccountDetailsView(this, currentAccount);
    },

    username:function(){
        $('#usernamediv').html("Hello, " + loginStatus.attributes.username);
    }

});