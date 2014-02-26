package com.validator;

import com.model.Transaction;
import com.service.TransactionService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TransactionValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Transaction.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Transaction t = (Transaction) o;
        if(t.getDestAccountId() <= 0){
            errors.rejectValue("destAccountId", "nonpositive", "Destination account id must be more than 0");
        }
        if(t.getMoneyAmount() <= 0){
            errors.rejectValue("moneyAmount", "nonpositive", TransactionService.TransactionStatus.WRONG_MONEY_AMOUNT.message());
        }
    }

}
