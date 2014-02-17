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