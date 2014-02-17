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
                switch (data.attributes.role){
                    case "Client": location.hash = "#transactions";
                                    break;
                    case "Employee": location.hash = "#accounts";
                                    break;
                }
            },   error: function(){
                console.log("something wrong");
                //Properly data of wrong login/password will be here
            }
        });

    }
});