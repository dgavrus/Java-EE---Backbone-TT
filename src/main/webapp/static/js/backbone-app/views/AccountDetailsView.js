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

    render:function(){
        $('#detailsModalBody').html(this.template());
        var modalTemplate = $.templates('#modalTemplate');
        modalTemplate.link(this.$('#modalResult'), this.account.toJSON());
        var modalTransactionsTemplate = $.templates('#modalTransactionsTemplate');
        modalTransactionsTemplate.link(this.$('#modalTransactions'), this.lastTransactions.toJSON());
        $('#detailsModal').modal('show');
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
        this.account.set({status: status});
        this.account.save(null, {
            success: function(){
                $('#status' + self.account.attributes.accountNumber).html(status);
                $('#status' + self.account.attributes.accountNumber).css('color', status == 'Active' ? 'green' : 'red');
                var accountDetailsView = new AccountDetailsView(self.parent, self.account);
                self.hide();
            }
        });
    }
});