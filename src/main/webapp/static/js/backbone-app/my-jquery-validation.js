function transactionFormValidate(field){
    var message;
    if(field[0].name == "moneyAmount" && field.val() <= 0){
        return "* Money amount should be more than 0"
    }
    var data = {};
    data[field[0].name] = field.val();
    console.log(!(field.val().length == 0));
    if(!(field.val().length == 0)){
        $.ajax({
            url: "/rest/transaction/validation",
            async: false,
            method: "GET",
            dataType: "text",
            data: data,
            success: function(){

            },
            error: function(response){
                message = response.responseText;
            }
        });
    }
    if(message){
        return message;
    }
}