package com.validator;

import com.model.Transaction;
import com.service.TransactionService;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> getErrorMessages(Errors errors){
        List<ObjectError> objectErrors = errors.getAllErrors();
        List<String> result = new ArrayList<String>();
        for(ObjectError oe : objectErrors){
            result.add(oe.getDefaultMessage());
        }
        return result;
    }

}
