package com.service;

import com.dao.AccountDAOdb;
import com.dao.TransactionDAOdb;
import com.model.Account;
import com.model.Transaction;
import javafx.event.ActionEvent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.*;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.internal.State;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
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
        Field f1 = transactionService.getClass().getDeclaredField("accountDAOdb");
        f1.setAccessible(true);
        f1.set(transactionService, accountDAOdb);

        Field f2 = transactionService.getClass().getDeclaredField("transactionDAOdb");
        f2.setAccessible(true);
        f2.set(transactionService, transactionDAOdb);
    }

    @Test
    public void notEnoughMoney() throws Exception {
        final Transaction t12 = new Transaction(1,2,5,new Date());
        final Transaction t21 = new Transaction(2,1,5,new Date());
        context.checking(new Expectations() {{
            Account source = new Account(1, 0, 1, 1, Account.Status.Active);
            Account dest = new Account(2, 5000, 2, 2, Account.Status.Active);
            oneOf(accountDAOdb).getAccount(1);
            will(returnValue(source));
            oneOf(accountDAOdb).getAccount(2);
            will(returnValue(dest));
        }});

        assertEquals(TransactionService.TransactionEnding.NOT_ENOUGH_MONEY, transactionService.makeTransaction(t12));
    }

    @Test
    public void Successful() throws Exception {

        final Transaction t12 = new Transaction(1,2,5,new Date());

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

        assertEquals(TransactionService.TransactionEnding.SUCCESSFUL, transactionService.makeTransaction(t12));
    }

    @Test
    public void newSource() throws Exception {
        final Transaction t12 = new Transaction(1,2,5,new Date());
        final Transaction t21 = new Transaction(2,1,5,new Date());
        context.checking(new Expectations() {{
            Account source = new Account(1, 5, 1, 1, Account.Status.New);
            Account dest = new Account(2, 5000, 2, 2, Account.Status.Active);
            oneOf(accountDAOdb).getAccount(1);
            will(returnValue(source));
            oneOf(accountDAOdb).getAccount(2);
            will(returnValue(dest));
        }});

        assertEquals(TransactionService.TransactionEnding.SOURCE_NOT_ACTIVATED, transactionService.makeTransaction(t12));
    }
    @Test
    public void newDest() throws Exception {
        final Transaction t12 = new Transaction(1,2,5,new Date());
        final Transaction t21 = new Transaction(2,1,5,new Date());
        context.checking(new Expectations() {{
            Account source = new Account(1, 5, 1, 1, Account.Status.Active);
            Account dest = new Account(2, 5000, 2, 2, Account.Status.New);
            oneOf(accountDAOdb).getAccount(1);
            will(returnValue(source));
            oneOf(accountDAOdb).getAccount(2);
            will(returnValue(dest));
        }});
        assertEquals(TransactionService.TransactionEnding.DEST_NOT_ACTIVATED, transactionService.makeTransaction(t12));
    }

    @Test
    public void wrongMoney() throws Exception {

        final Transaction t12 = new Transaction(1,2,-4,new Date());

        context.checking(new Expectations() {{
            Account source = new Account(1, 5, 1, 1, Account.Status.Active);
            Account dest = new Account(2, 5000, 2, 2, Account.Status.Active);
            oneOf(accountDAOdb).getAccount(1);
            will(returnValue(source));
            oneOf(accountDAOdb).getAccount(2);
            will(returnValue(dest));
        }});

        assertEquals(TransactionService.TransactionEnding.WRONG_MONEY_AMOUNT, transactionService.makeTransaction(t12));
    }
}