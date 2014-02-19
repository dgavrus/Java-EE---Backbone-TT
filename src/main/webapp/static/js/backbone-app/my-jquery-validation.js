function moneyAmountValidate(field){
    var message;
    if(field.val() <= 0){
        return "* Money amount should be more than 0"
    }
    $.ajax({
        url: "/rest/transaction/validation",
        async: false,
        method: "GET",
        dataType: "text",
        data: {moneyAmount: field.val()},
        success: function(){

        },
        error: function(response){
            message = "* " + response.responseText;
            return message;
        }
    });
    if(message){
        return message;
    }
}

function destAccValidate(field){
    var message;
    if(!field.val().length == 0){
        $.ajax({
            url: "/rest/transaction/validation",
            async: false,
            method: "GET",
            dataType: "text",
            data: {destAccId: field.val()},
            success: function(){

            },
            error: function(response){
                message = "* " + response.responseText;
                return message;
            }
        });
    }
    if(message){
        return message;
    }
}