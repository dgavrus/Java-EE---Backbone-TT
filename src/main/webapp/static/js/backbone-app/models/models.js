var LoginStatusModel = Backbone.Model.extend({
    defaults:{
        username: 'sign in please',
        password: '',
        loggedIn: false,
        role: null
    },
    // Url binding of the REST service
    url:'rest/login'

});

var AccountModel = Backbone.Model.extend({
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

var TransactionModel = Backbone.Model.extend({
    defaults:{
        sourceAccountId: null,
        destAccountId: null,
        moneyAmount: null,
        date: null
    },

    url:'rest/transaction'

});

var TransactionList = Backbone.Collection.extend({
    model: TransactionModel,
    url: "rest/transaction"
});

var AccountList = Backbone.Collection.extend({
    model: AccountModel,
    url: "rest/userslist"
});

var PaginationInfoModel = Backbone.Model.extend({

    initialize: function(params){
        this.url = params.url;
    },

    defaults:{
        pagesCount: null,
        activePage: null,
        pagesForView: null,
        rowsPerPage: null
    },

    url: function(){
        return this.url;
    }
});