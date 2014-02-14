/******************* Models & Collections *******************/

var accountsView;

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
        status: null
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

/*********************************************/

/******************* Views *******************/


window.AccountDetailsView = Backbone.View.extend({

    initialize:function(parent){
        this.parent = parent;
    },

    render:function(parent){
        $(this.el).html(this.template());
        var modalTemplate = $.templates('#modalTemplate');
        modalTemplate.link(this.$('#modalResult'), this.parent.collection.toJSON());
        return this;
    }

});

window.LoginView = Backbone.View.extend({

    initialize:function () {
        $('#usernamediv').html("Hello, sign in please");
        this.render();
        console.log('Initializing Login View');
    },

    render:function () {
        $(this.el).html(this.template());
        return this;
    },

    events : {
        "click #signInButton" : "signIn"
    },

    signIn: function(){
        loginStatus.set({'username':this.$("#j_username").val(),
            'password':this.$("#j_password").val(),
            'loggedIn':false});
        loginStatus.save(null, {
            success: function(data){
                console.log(data);
                if(data.attributes.role == "Client"){
                    location.hash = "#transactions";
                }
                else if(data.attributes.role == "Employee"){
                    location.hash = "#accounts";
                }
            },   error: function(){
                console.log("something wrong");
             //Properly data of wrong login/password will be here
            }
        });

    }
});

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
        "click .dropdown-menu li" : "changeStatus",
        "click .btn" : "showDetails"
    },

    changeStatus: function(e){
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
    },

    showDetails: function(){
        var self = this;
        var accountDetailsView = new AccountDetailsView(this);
        var modal = new Backbone.BootstrapModal({
            content:accountDetailsView,
            allowCancel:false,
            hidden: function(){
                self.render();
            }
        });
        modal.open();
        /*var accountDetailsView = new AccountDetailsView();
        $('#detailsModalBody').html(accountDetailsView.template());
        accountDetailsView.render();
        var modalTemplate = $.templates('#modalTemplate');
        $('#detailsModal').modal();*/
    },

    username:function(){
        $('#usernamediv').html("Hello, " + loginStatus.attributes.username);
    }

});

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

window.LogoutView = Backbone.View.extend({

    el: "#navbardiv",

    initialize:function () {
        this.render();
        console.log('Initializing Logout View');
        console.log(this.el);
    },

    render:function () {
        $("#navbardiv").append(this.template());
        return this;
    },

    events : {
        "click #logout" : "logout"
    },

    logout: function(){
        loginStatus.save(null, {
            success: function(){
                console.log("logout success");
                location.href = "/page";
            }
        });
        loginStatus.fetch();
        console.log("hello");
    }
});

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

    getPages: function(startPage, lastPage){
        var sp, lp;
        var pages = "[";
        var current = this.paginationParams.attributes.activePage;
        var pagesForView = this.paginationParams.attributes.pagesForView;
        var pagesCount = this.paginationParams.attributes.pagesCount;
        var pageName;
        if(this.paginationParams.attributes.url === "/rest/transaction/pagination"){
            pageName = "transactions";
        } else if(this.paginationParams.attributes.url === "/rest/userslist/pagination"){
            pageName = "accounts";
        }
        if(!startPage && !lastPage){
            sp = 1, lp = pagesForView;
        } else {
            sp = startPage, lp = lastPage;
        }
        while(current > lp && lp < pagesCount){
            sp++, lp++;
        }
        while(current < sp && sp > 1){
            sp--, lp--;
        }
        var liClass = current <= 1 ? "disabled" : "default";
        pages += "{\"pageName\":\"" + pageName + "\",\"liClass\":\"" + liClass + "\",\"pageNumberText\":\"prev\",\"pageNumber\":" + (current <= 1 ? current : (parseInt(current) - 1)) + "},";
        for(var i = sp; i <= lp; i++){
            liClass = current == i ? "active" : "default";
            pages += "{\"current\":" + current + ",\"pageName\":\"" + pageName + "\",\"liClass\":\"" + liClass + "\",\"pageNumberText\":" + i + ",\"pageNumber\":" + i +"},";
        }
        liClass = current >= pagesCount ? "disabled" : "default";
        pages += "{\"current\":" + current + ",\"pageName\":\"" + pageName + "\",\"liClass\":\"" + liClass + "\",\"pageNumberText\":\"next\",\"pageNumber\":" + (current >= pagesCount ? current : (parseInt(current) + 1)) + "}";
        pages += "]";
        console.log(pages);
        return $.parseJSON(pages);
    }

});

/*********************************************/

window.templateLoader = {

    load: function(views, callback) {

        var deferreds = [];

        $.each(views, function(index, view) {
            if (window[view]) {
                deferreds.push($.get('static/views/' + view + '.html', function(data) {
                    window[view].prototype.template = _.template(data);
                }, 'html'));
            } else {
                alert(view + " not found");
            }
        });

        $.when.apply(null, deferreds).done(callback);
    }

};

var loginStatus = new LoginStatus({username:"sign in please"});

window.Router = Backbone.Router.extend({

    routes: {
        "": "login",
        "accounts": "accounts",
        "accounts/page/:pageNumber": "accounts",
        "transactions": "transactions",
        "transactions/page/:pageNumber":"transactions"
    },

    initialize: function () {
        /*this.login = new LoginView();
        this.LoginView.render()*/
    },

    login: function() {
        if(!loginStatus){
            loginStatus = new LoginStatus();
            var self = this;
            loginStatus.fetch({
                success: function(){
                    console.log(loginStatus.attributes.loggedIn);
                    if(loginStatus.attributes.loggedIn){
                        switch (loginStatus.attributes.role){
                            case 'Client': window.location.hash = "#transactions";
                            case 'Employee': window.location.hash = "#accounts";
                        }
                    } else {
                        console.log("hmmm...");
                    }
                },
                error: function(){
                    console.log("error fetch");
                }
            });
        }
        if (!this.loginView) {
            this.loginView = new LoginView();
            this.loginView.render();
        }
        $('#content').html(this.loginView.el);
    },

    accounts: function (page) {
        var self = this;
        if(!page){
            page = 1;
        }
        var pages = new PaginationInfo({url:"rest/userslist/pagination"});
        pages.fetch({
            success: function(){
               if(!isValidPage(self, page, pages)){
                    page = pages.attributes.activePage;
                    location.hash = "#accounts/page/" + page;
                }

                pages.attributes.activePage = page;
                pages.save();

                loginStatus = new LoginStatus();
                loginStatus.fetch();

                if(!self.accountList){
                    self.accountList = new AccountList();
                }

                self.accountList.fetch({
                    data: $.param({page: page}),
                    success: function(data){
                        successFetch(data, self, pages)
                    },
                    error: function(err, xhr, status){
                        errorFetch(xhr);
                    }
                });

            },
            error: function(err, xhr, status){
                errorFetch(xhr);
        }});

    },

    transactions: function (page) {
        var self = this;
        if(!page){
            page = 1;
        }
        var pages = new PaginationInfo({url:"rest/transaction/pagination"});
        pages.fetch({
            success: function(){
                if(!isValidPage(self, page, pages)){
                    page = pages.attributes.activePage;
                    location.hash = "#transactions/page/" + page;
                }

                pages.attributes.activePage = page;
                pages.save();

                loginStatus = new LoginStatus();
                loginStatus.fetch();

                if(!self.userTransactions){
                    self.userTransactions = new TransactionList();
                }

                self.userTransactions.fetch({
                    data: $.param({ page: page}),
                    success: function(data){
                        successFetch(data, self, pages)
                    },
                    error: function(err, xhr, status){
                        errorFetch(xhr);
                    }
                });

                },
            error: function(err, xhr, status){
                errorFetch(xhr);
            }});
    }
});

templateLoader.load(["LoginView", "AccountsView", "TransactionsView", "LogoutView", "PaginationView", "AccountDetailsView"],
    function () {
        app = new Router();
        Backbone.history.start();
    });

function successFetch(data, self, pages, view){
    if(!self.logoutView){
        self.logoutView = new LogoutView();
    }
    if(!this.paginationView){
        this.paginationView = new PaginationView({paginationParams:pages});
    }
    this.paginationView.render();
    this.paginationView.paginationParams.set({activePage:pages.attributes.activePage});
    if(pages.attributes.url.search("transaction") > 0){
        if (!self.transactionsView) {
            self.transactionsView = new TransactionsView({collection:self.userTransactions,pagination:this.paginationView});
            self.transactionsView.render();
            $('#content').html(self.transactionsView.el);
        }
    } else {
        if (!self.accountsView) {
            self.accountsView = new AccountsView({collection:self.accountList,pagination:this.paginationView});
            self.accountsView.render();
            $('#content').html(self.accountsView.el);
        }
    }
    console.log("success");
}

function errorFetch(){
    console.log("error");
    location.href = "/page";
}

function isValidPage(self, page, pages){
    return page <= pages.attributes.pagesCount && page >= 1;
}