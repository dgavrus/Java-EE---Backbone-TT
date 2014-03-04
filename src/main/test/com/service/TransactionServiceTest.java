package com.service;

import com.dao.AccountDAOdb;
import com.dao.TransactionDAOdb;
import com.model.Account;
import com.model.Transaction;
import org.jmock.Expectations;

import static org.junit.Assert.*;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.validation.BindingResult;

import java.util.Date;

public class TransactionServiceTest {

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    TransactionDAOdb transactionDAOdb;
    AccountDAOdb accountDAOdb;
    TransactionService transactionService = new TransactionService();

    @Before
    public void before() throws Exception {

        transactionDAOdb = context.mock(TransactionDAOdb.class);
        accountDAOdb = context.mock(AccountDAOdb.class);
        transactionService.setAccountDAOdb(accountDAOdb);
        transactionService.setTransactionDAOdb(transactionDAOdb);
    }

    @Test
    public void notEnoughMoney() throws Exception {
        final Transaction t12 = new Transaction(1,2,5,new DateTime());
        context.checking(new Expectations() {{
            Account source = new Account(1, 0, 1, 1, Account.Status.Active);
            Account dest = new Account(2, 5000, 2, 2, Account.Status.Active);
            oneOf(accountDAOdb).getAccount(1);
            will(returnValue(source));
            oneOf(accountDAOdb).getAccount(2);
            will(returnValue(dest));
        }});

        assertEquals(TransactionService.TransactionStatus.NOT_ENOUGH_MONEY, transactionService.makeTransaction(t12));
    }

    @Test
    public void Successful() throws Exception {

        final Transaction t12 = new Transaction(1,2,5,new DateTime());

        context.checking(new Expectations() {{
            Account source = new Account(1, 5, 1, 1, Account.Status.Active);
            Account dest = new Account(2, 5000, 2, 2, Account.Status.Active);
            oneOf(accountDAOdb).getAccount(1);
            will(returnValue(source));
            oneOf(accountDAOdb).getAccount(2);
            will(returnValue(dest));
            oneOf(accountDAOdb).updateAccount(source);
            oneOf(accountDAOdb).updateAccount(dest);
            oneOf(transactionDAOdb).addTransaction(with(any(Transaction.class)));
        }});

        assertEquals(TransactionService.TransactionStatus.SUCCESSFUL, transactionService.makeTransaction(t12));
    }

    @Test
    public void newSource() throws Exception {
        final Transaction t12 = new Transaction(1,2,5,new DateTime());
        context.checking(new Expectations() {{
            Account source = new Account(1, 5, 1, 1, Account.Status.New);
            Account dest = new Account(2, 5000, 2, 2, Account.Status.Active);
            oneOf(accountDAOdb).getAccount(1);
            will(returnValue(source));
            oneOf(accountDAOdb).getAccount(2);
            will(returnValue(dest));
        }});

        assertEquals(TransactionService.TransactionStatus.SOURCE_NOT_ACTIVATED, transactionService.makeTransaction(t12));
    }
    @Test

    public void newDest() throws Exception {
        final Transaction t12 = new Transaction(1,2,5,new DateTime());
        context.checking(new Expectations() {{
            Account source = new Account(1, 5, 1, 1, Account.Status.Active);
            Account dest = new Account(2, 5000, 2, 2, Account.Status.New);
            oneOf(accountDAOdb).getAccount(1);
            will(returnValue(source));
            oneOf(accountDAOdb).getAccount(2);
            will(returnValue(dest));
        }});
        assertEquals(TransactionService.TransactionStatus.DEST_NOT_ACTIVATED, transactionService.makeTransaction(t12));
    }

    @Test
    public void wrongMoneyFalseTest() {
        final Transaction t12 = new Transaction(1,2,1,new DateTime());
        context.checking(new Expectations() {{
            Account source = new Account(1, 1, 1, 1, Account.Status.Active);
            Account dest = new Account(2, 0, 2, 2, Account.Status.New);
            oneOf(accountDAOdb).getAccount(1);
            will(returnValue(source));
            oneOf(accountDAOdb).getAccount(2);
            will(returnValue(dest));
        }});
        assertFalse("This account has enough money", transactionService.makeTransaction(t12).equals(TransactionService.TransactionStatus.WRONG_MONEY_AMOUNT));
    }


}