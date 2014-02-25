package com.service;

import com.dao.AccountDAOdb;
import com.dao.TransactionDAOdb;
import com.model.Account;
import com.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private AccountDAOdb accountDAOdb;

    @Autowired
    private TransactionDAOdb transactionDAOdb;

    private boolean isEnoughMoney(Account account, long moneyRequired){
        return account.getMoneyAmount() >= moneyRequired;
    }

    private TransactionStatus makeTransaction(int sourceAccountId, int destAccountId,
                                long moneyAmount) {

        Account source = accountDAOdb.getAccount(sourceAccountId);
        Account dest = accountDAOdb.getAccount(destAccountId);

        if(dest == null){
            return TransactionStatus.DEST_ACCOUNT_NOT_EXISTS;
        } else if(!isEnoughMoney(source, moneyAmount)){
            return TransactionStatus.NOT_ENOUGH_MONEY;
        } else if(!source.isActive()){
            return TransactionStatus.SOURCE_NOT_ACTIVATED;
        } else if(!dest.isActive()){
            return TransactionStatus.DEST_NOT_ACTIVATED;
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
        return TransactionStatus.SUCCESSFUL;
    }

    public TransactionStatus makeTransaction(Transaction transaction) {
        long moneyAmount = transaction.getMoneyAmount();
        int sourceAccountId = transaction.getSourceAccountId();
        int destAccountId = transaction.getDestAccountId();
        return makeTransaction(sourceAccountId, destAccountId, moneyAmount);
    }

    public List<Transaction> userTransactionList(int accountId, int page, int count){
        return transactionDAOdb.userTransactionList(accountId, page, count);
    }

    public List<Transaction> userTransactionListDesc(int accountId, int count){
        return transactionDAOdb.userTransactionListDesc(accountId, count);
    }

    public enum TransactionStatus {
        WRONG_MONEY_AMOUNT {
            public String message(){
                return "* Money amount should be more than 0";
            }
        },
        NOT_ENOUGH_MONEY {
            public String message(){
                return "* Money amount is not enough for making transaction";
            }
        },
        SOURCE_NOT_ACTIVATED {
            public String message(){
                return "* Your account is not activated";
            }
        },
        DEST_NOT_ACTIVATED {
            public String message(){
                return "* Destination account is not activated";
            }
        },
        DEST_ACCOUNT_NOT_EXISTS {
            public String message(){
                return "* Destination account is not exists";
            }
        },
        SUCCESSFUL {
            public String message(){
                return "Transaction completed";
            }
        };
        public abstract String message();
    }

    public void setTransactionDAOdb(TransactionDAOdb transactionDAOdb){
        this.transactionDAOdb = transactionDAOdb;
    }

    public void setAccountDAOdb(AccountDAOdb accountDAOdb){
        this.accountDAOdb = accountDAOdb;
    }

}
