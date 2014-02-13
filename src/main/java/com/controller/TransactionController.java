package com.controller;

import com.dao.AccountDAOdb;
import com.dao.TransactionDAOdb;
import com.dao.UserDAOdb;
import com.model.PaginationInfo;
import com.model.Transaction;
import com.model.User;
import com.service.TransactionService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class TransactionController {

    private final String url = "/rest/transaction/pagination";

    @Autowired
    UserDAOdb userDAOdb;

    @Autowired
    AccountDAOdb accountDAOdb;

    @Autowired
    TransactionDAOdb transactionDAOdb;

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/rest/transaction", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Transaction> getUserTransactions(HttpServletRequest request){
        User authenticatedUser = userService.getAuthenticatedUser();
        int pageNumber;
        try{
            pageNumber = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e){
            return transactionDAOdb.userTransactionList(authenticatedUser.getAccountId(), 1);
        }
        return transactionDAOdb.userTransactionList(authenticatedUser.getAccountId(), pageNumber);
    }

    @RequestMapping(value = "/rest/transaction", method = RequestMethod.POST)
    public void transaction(@RequestBody Transaction currentTransaction,
                            HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        currentTransaction.setSourceAccountId(userService.getAuthenticatedUser().getAccountId());
        currentTransaction.setDate(new Date());
        transactionService.makeTransaction(currentTransaction);
    }

    @RequestMapping(value = "/rest/transaction/pagination", method = RequestMethod.GET)
    public @ResponseBody PaginationInfo pagination(){
        User authenticatedUser = userService.getAuthenticatedUser();
        int pages = (int)Math.ceil(transactionDAOdb.getUserTransactionsCount(authenticatedUser.getAccountId()) / 10.0);
        if(this.pagination == null){
            this.pagination = new PaginationInfo(pages, 1, Math.min(pages, 7), url);
        }
        this.pagination.setPagesForView(Math.min(pages, 7));
        this.pagination.setPagesCount(pages);
        return this.pagination;
    }

    @RequestMapping(value = "/rest/transaction/pagination", method = RequestMethod.POST)
    public @ResponseBody void paginationPost(@RequestBody PaginationInfo pagination){
        this.pagination = pagination;
    }

    private PaginationInfo pagination;

}
