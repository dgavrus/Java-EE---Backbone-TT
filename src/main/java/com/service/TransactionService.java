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

    private boolean isEnoughMoney(Account account, long moneyRequired){
        return account.getMoneyAmount() >= moneyRequired;
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
        } else if(!isEnoughMoney(source, moneyAmount)){
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
        WRONG_MONEY_AMOUNT {
            public String message(){
                return "Money amount should be more than 0";
            }
        },
        NOT_ENOUGH_MONEY {
            public String message(){
                return "Money amount is not enough for making transaction";
            }
        },
        SOURCE_NOT_ACTIVATED {
            public String message(){
                return "Your account is not activated";
            }
        },
        DEST_NOT_ACTIVATED {
            public String message(){
                return "Destination account is not activated";
            }
        },
        DEST_ACCOUNT_NOT_EXISTS {
            public String message(){
                return "Destination account is not exists";
            }
        },
        SUCCESSFUL {
            public String message(){
                return "Transaction completed";
            }
        };
        public abstract String message();
    }

}
