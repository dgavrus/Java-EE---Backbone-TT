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

    private boolean isNotLockedAndActivated(int accountId1, int accountId2){
        Account account1 = accountDAOdb.getAccount(accountId1);
        Account account2 = accountDAOdb.getAccount(accountId2);
        if(account1.isActive() && account2.isActive()){
            return true;
        }
        return false;
    }

    private String makeTransaction(int sourceAccountId, int destAccountId,
                                long moneyAmount) {

            if(moneyAmount <= 0){
                return "Wrong money amount!";
            } else if(!isEnoughMoney(sourceAccountId, moneyAmount)){
                return "You haven't enough money on " +
                        "your balance for this operation";
            } else if(!isNotLockedAndActivated(sourceAccountId, destAccountId)){
                return "Source or destination account " +
                        "is locked or not activated";
            }

        Account source = accountDAOdb.getAccount(sourceAccountId);
        Account dest = accountDAOdb.getAccount(destAccountId);
        source.setMoneyAmount(source.getMoneyAmount() - moneyAmount);
        dest.setMoneyAmount(dest.getMoneyAmount() + moneyAmount);
        accountDAOdb.updateAccount(source);
        accountDAOdb.updateAccount(dest);
        transactionDAOdb.addTransaction(
                new Transaction(sourceAccountId,
                        destAccountId,
                        moneyAmount, new Date())
        );
        return "Successful";
    }

    public String makeTransaction(Transaction transaction) {
        long moneyAmount = transaction.getMoneyAmount();
        int sourceAccountId = transaction.getSourceAccountId();
        int destAccountId = transaction.getDestAccountId();
        return makeTransaction(sourceAccountId, destAccountId, moneyAmount);
    }

}
