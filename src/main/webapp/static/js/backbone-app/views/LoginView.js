window.LoginView = Backbone.View.extend({

    initialize:function () {
        $('#usernamediv').html("Hello, sign in please");
        this.render();
        console.log('Initializing Login View');
        /*loginStatus.on('sync', this.username, this);*/
        $('#signIn').validationEngine();
    },

    render:function () {
        $(this.el).html(this.template());
        return this;
    },

    events : {
        "click #signInButton" : "signIn"
    },

    signIn: function(){
        $('#signIn').validationEngine('hide');
        loginStatus.set({'username':this.$("#j_username").val(),
            'password':this.$("#j_password").val(),
            'loggedIn':false});
        loginStatus.save(null, {
            success: function(data, response){
                console.log(data);
                switch (data.attributes.role){
                    case "Client": app.navigate("#transactions", {trigger: true});
                                    break;
                    case "Employee": app.navigate("#accounts", {trigger: true});
                                    break;
                }
            },
            error: function(data, response){
                console.log("something wrong");
                if(response.responseJSON.username == null){
                    $('#j_username').validationEngine('showPrompt', 'User not found') ;
                } else if(!response.responseJSON.loggedIn){
                    $('#j_password').validationEngine('showPrompt', 'Wrong password') ;
                }
            }
        });

    },
    /*username:function(){
        $('#usernamediv').html("Hello, sign in please");
    }*/
});