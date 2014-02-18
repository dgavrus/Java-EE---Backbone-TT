package com.service;

import com.dao.AccountDAOdb;
import com.dao.AccountDAOdbImpl;
import com.dao.TransactionDAOdb;
import com.dao.TransactionDAOdbImpl;
import com.model.Account;
import com.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionService {

    @Autowired
    protected AccountDAOdb accountDAOdb;

    @Autowired
    TransactionDAOdb transactionDAOdb;

    private boolean isEnoughMoney(int accountId, long moneyRequired){
        Account current = accountDAOdb.getAccount(accountId);
        return current.getMoneyAmount() >= moneyRequired;
    }

    private boolean isActive(int accountId){
        Account account = accountDAOdb.getAccount(accountId);
        return account.isActive();
    }

    private TransactionEnding makeTransaction(int sourceAccountId, int destAccountId,
                                long moneyAmount) {

        Account source = accountDAOdb.getAccount(sourceAccountId);
        Account dest = accountDAOdb.getAccount(destAccountId);
        if(dest == null){
            return TransactionEnding.DEST_ACCOUNT_NOT_EXISTS;
        }
        if(moneyAmount <= 0){
            return TransactionEnding.WRONG_MONEY_AMOUNT;
        } else if(!isEnoughMoney(sourceAccountId, moneyAmount)){
            return TransactionEnding.NOT_ENOUGH_MONEY;
        } else if(!source.isActive()){
            return TransactionEnding.SOURCE_NOT_ACTIVATED;
        } else if(!dest.isActive()){
            return TransactionEnding.DEST_NOT_ACTIVATED;
        }

        source.setMoneyAmount(source.getMoneyAmount() - moneyAmount);
        dest.setMoneyAmount(dest.getMoneyAmount() + moneyAmount);
        accountDAOdb.updateAccount(source);
        accountDAOdb.updateAccount(dest);
        transactionDAOdb.addTransaction(
                new Transaction(sourceAccountId,
                        destAccountId,
                        moneyAmount, new Date())
        );
        return TransactionEnding.SUCCESSFUL;
    }

    public TransactionEnding makeTransaction(Transaction transaction) {
        long moneyAmount = transaction.getMoneyAmount();
        int sourceAccountId = transaction.getSourceAccountId();
        int destAccountId = transaction.getDestAccountId();
        return makeTransaction(sourceAccountId, destAccountId, moneyAmount);
    }

    public enum TransactionEnding {
        WRONG_MONEY_AMOUNT,
        NOT_ENOUGH_MONEY,
        SOURCE_NOT_ACTIVATED,
        DEST_NOT_ACTIVATED,
        DEST_ACCOUNT_NOT_EXISTS,
        SUCCESSFUL
    }

}
