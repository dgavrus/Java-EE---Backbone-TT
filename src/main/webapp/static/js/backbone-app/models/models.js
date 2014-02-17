var LoginStatus = Backbone.Model.extend({
    defaults:{
        username: '',
        password: '',
        loggedIn: false,
        role: null
    },
    // Url binding of the REST service
    url:'rest/login'

});

var Account = Backbone.Model.extend({
    defaults:{
        id: null,
        moneyAmount: null,
        userId: null,
        accountNumber: null,
        status: null,
        ownerFirstName: null,
        ownerLastName: null
    },
    url: 'rest/userslist'
});

var Transaction = Backbone.Model.extend({
    defaults:{
        sourceAccountId: null,
        destAccountId: null,
        moneyAmount: null,
        date: null
    },

    url:'rest/transaction'

});

var TransactionList = Backbone.Collection.extend({
    model: Transaction,
    url: "rest/transaction"
});

var AccountList = Backbone.Collection.extend({
    model: Account,
    url: "rest/userslist"
});

var PaginationInfo = Backbone.Model.extend({

    initialize: function(params){
        this.url = params.url;
    },

    defaults:{
        pagesCount: null,
        activePage: null,
        pagesForView: null
    },

    url: function(){
        return this.url;
    }
});