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
        var self = this;
        loginStatus.save(null, {
            success: function(){
                console.log("logout success");
                loginStatus.fetch({
                    success: function(){
                        $("#logoutForm").remove();
                        app.navigate("", {trigger: true});
                    }
                });
            }
        });

        console.log("hello");
    }
});