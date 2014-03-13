window.AccountDetailsView = Backbone.View.extend({

    el: '#detailsModalBody',

    initialize:function(parent, currentAccount){
        var self = this;
        this.lastTransactions = new TransactionList();
        this.lastTransactions.url = "rest/userslist/transaction";
        this.lastTransactions.fetch({
            data: $.param({ accountNumber: currentAccount.attributes.accountNumber}),
            success: function(){
                self.account = currentAccount;
                self.parent = parent;
                self.render();
            }
        });
    },

    render:function(account){
        $('#detailsModalBody').html(this.template());
        var modalTemplate = $.templates('#modalTemplate');
        modalTemplate.link(this.$('#modalResult'), this.account.toJSON());
        var modalTransactionsTemplate = $.templates('#modalTransactionsTemplate');
        modalTransactionsTemplate.link(this.$('#modalTransactions'), this.lastTransactions.toJSON());
        if(!account){
            $('#detailsModal').modal('show');
        }
        var self = this;
        $('#active').click(function(){
            self.changeStatus("Active");
        });
        $('#blocked').click(function(){
            self.changeStatus("Blocked");
        });
        return this;
    },

    changeStatus: function(status){
        var self = this;
        var oldStatus = this.account.attributes.status;
        this.account.save({status: status}, {
            success: function(account){
                $('#status' + self.account.attributes.accountNumber).html(status);
                $('#status' + self.account.attributes.accountNumber).css('color', status == 'Active' ? 'green' : 'red');
                self.render(account);
            },
            error: function(message, response){
                self.account.set('status', oldStatus);
                $('.dropdown-menu').validationEngine('showPrompt', response.responseText);
            }
        });
    }
});