window.TransactionsView = Backbone.View.extend({

    initialize:function (params) {
        this.pagination = params.pagination;
        this.collection.on('sync', this.render, this);
        this.pagination.paginationParams.on('sync',this.render, this);
        loginStatus.on('sync', this.username, this);
        loginStatus.fetch();
        console.log('Initializing Transactions View');
        console.log(this.el);

    },

    render:function () {
        $(this.el).html(this.template());
        var template = $.templates("#transactionsTemplate");
        template.link(this.$("#transactionsTableData"), this.collection.toJSON());
        $("#paginationdiv").html(this.pagination.template());
        var pageTemplate = $.templates("#pageTemplate");
        pageTemplate.link(this.$("#tpagination"), this.pagination.getPages());
        return this;
    },

    events : {
        "click #transactionButton" : "makeTransaction",
        "change" : "updateUserTransactions"
    },

    makeTransaction : function() {
        var currentTransaction = new Transaction({
            destAccountId:$('#destAcc').val(),
            moneyAmount:$('#moneyAmount').val()
        })
        currentTransaction.save();
        this.pagination.paginationParams.fetch();
        this.collection.fetch({
            data: $.param({page: this.pagination.paginationParams.attributes.activePage})
        });
        this.render();
    },

    username:function(){
        $('#usernamediv').html("Hello, " + loginStatus.attributes.username);
    }

});