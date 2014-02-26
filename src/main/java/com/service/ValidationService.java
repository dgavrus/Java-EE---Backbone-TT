package com.service;

import com.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ValidationService {

    public final String MONEY_AMOUNT_PARAM = "moneyAmount";

    public final String DEST_ACC_PARAM = "destAcc";

    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;

    public ResponseEntity<String> transactionFieldsValidate(Map<String, String[]> parameters){
        if(parameters.containsKey(MONEY_AMOUNT_PARAM)){
            Integer money;
            money = Integer.parseInt(parameters.get(MONEY_AMOUNT_PARAM)[0]);
            Account source = accountService.getAccount(userService.getAuthenticatedUser().getAccountId());
            if(!source.isActive()){
                return new ResponseEntity<String>(
                        TransactionService.TransactionStatus.SOURCE_NOT_ACTIVATED.message(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            else if(source.getMoneyAmount() < money){
                return new ResponseEntity<String>(
                        TransactionService.TransactionStatus.NOT_ENOUGH_MONEY.message(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if(parameters.containsKey(DEST_ACC_PARAM)){
            Integer destAccId = Integer.parseInt(parameters.get(DEST_ACC_PARAM)[0]);
            Account dest = accountService.getAccount(destAccId);
            if(dest != null){
                if(!dest.isActive()){
                    return new ResponseEntity<String>(
                            TransactionService.TransactionStatus.DEST_NOT_ACTIVATED.message(),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<String>(
                        TransactionService.TransactionStatus.DEST_ACCOUNT_NOT_EXISTS.message(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity(TransactionService.TransactionStatus.SUCCESSFUL.message(), HttpStatus.OK);
    }

}
