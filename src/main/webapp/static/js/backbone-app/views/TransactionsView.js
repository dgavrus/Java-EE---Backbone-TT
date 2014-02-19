window.TransactionsView = Backbone.View.extend({

    initialize:function (params) {
        this.pagination = params.pagination;
        this.collection.on('sync', this.render, this);
        this.pagination.paginationParams.on('sync',this.render, this);
        loginStatus.on('sync', this.username, this);
        loginStatus.fetch();
        $("#transactionForm").validationEngine();
        $('#transactionForm').validationEngine('attach', {'autoHidePrompt': 'true', 'autoHideDelay': 1000});
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
        "change" : "updateUserTransactions",
        "blur #destAcc": "destAccBlur",
        "blur #moneyAmount": "moneyAmountBlur"
    },

    makeTransaction : function() {
        $("#transactionForm").validationEngine('hide');
        if($("#transactionForm").validationEngine('validate')){
             var currentTransaction = new Transaction({
                destAccountId:$('#destAcc').val(),
                moneyAmount:$('#moneyAmount').val()
            })
            var self = this;
            currentTransaction.save(null, {
                success: function(obj, response){
                    self.pagination.paginationParams.fetch({
                        success: function(){
                            self.collection.fetch({
                                data: $.param({page: self.pagination.paginationParams.attributes.activePage})
                            });
                        }
                    });
                },
                error: function(obj, response){
                    var message = "* " + response.responseText;
                    $('#transactionForm').validationEngine('showPrompt', message);
                }
            });
        }
    },

    destAccBlur: function(){
        $("#transactionForm").validationEngine('hide');
        $('#destAcc').validationEngine();
        $('#destAcc').validationEngine('validate');
    },

    moneyAmountBlur: function(){
        $('#transactionForm').validationEngine('hide');
        $('#moneyAmount').validationEngine();
        $('#moneyAmount').validationEngine('validate');
    },

    username:function(){
        $('#usernamediv').html("Hello, " + loginStatus.attributes.username);
    }

});