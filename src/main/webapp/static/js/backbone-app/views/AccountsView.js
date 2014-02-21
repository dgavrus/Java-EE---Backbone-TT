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
        this.setStatusColor();
        return this;
    },

    events: {
        "click .glyphicon" : "showDetails"
    },

    showDetails: function(e){
        e.preventDefault();
        var accountId = e.target.parentNode.id;
        var self = this;
        var currentAccount = this.collection.findWhere({accountNumber: parseInt(accountId)});
        var accountDetailsView = new AccountDetailsView(this, currentAccount);
    },

    username:function(){
        $('#usernamediv').html("Hello, " + loginStatus.attributes.username);
    },

    setStatusColor:function(){
        for(i = 0; i < this.collection.length; i++){
            var item = this.collection.models[i];
            if(item.attributes.status == 'Active'){
                $('#status' + item.attributes.accountNumber).css('color','green');
            } else if(item.attributes.status == 'Blocked'){
                $('#status' + item.attributes.accountNumber).css('color','red');
            } else {
                $('#status' + item.attributes.accountNumber).css('color','blue');
            }
        }
    }
});