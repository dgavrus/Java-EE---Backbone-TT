window.TransactionsView = Backbone.View.extend({

    initialize:function (params) {
        this.pagination = params.pagination;
        this.collection.on('sync', this.render, this);
        this.pagination.paginationParams.on('sync',this.render, this);
        loginStatus.on('sync', this.username, this);
        loginStatus.fetch();
        $("#transactionForm").validationEngine();

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
        "blur #transactionForm": "transactionValidate"
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
                                data: $.param({page: self.pagination.paginationParams.attributes.activePage}),
                                success: function(){

                                },
                                error: function(){
                                    alert('error');
                                }
                            });
                        }
                    });

                },
                error: function(obj, response){
                    var message;
                    console.log(response.responseJSON);
                    console.log(response.responseJSON == "NOT_ACTIVATED")
                    switch(response.responseJSON){
                        case "SOURCE_NOT_ACTIVATED":
                            message = "Your account is not activated";
                            break;
                        case "DEST_NOT_ACTIVATED":
                            message = "Destination account is not activated";
                            break;
                        case "NOT_ENOUGH_MONEY":
                            message = "Money amount is not enough for make transaction";
                            break;
                        case "DEST_ACCOUNT_NOT_EXISTS":
                            message = "Destination account is not exists"
                            break;
                    }
                    $('#transactionForm').validationEngine('showPrompt', message);
                }
            });
        }
    },

    transactionValidate: function(){
        alert('onfocus aga');
    },

    username:function(){
        $('#usernamediv').html("Hello, " + loginStatus.attributes.username);
    }

});