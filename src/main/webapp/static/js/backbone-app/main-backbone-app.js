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

templateLoader.load(["LoginView", "AccountsView", "TransactionsView", "LogoutView", "PaginationView", "AccountDetailsView"],
    function () {
        app = new Router();
        Backbone.history.start();
    });

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