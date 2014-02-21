package com.controller;

import com.dao.*;
import com.model.*;
import com.service.TransactionService;
import com.service.UserService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class AccountListController {

    private final String url = "/rest/userslist/pagination";

    @Autowired
    UserDAOdb userDAOdb;

    @Autowired
    AccountDAOdb accountDAOdb;

    @Autowired
    UserService userService;

    @Autowired
    TransactionDAOdb transactionDAOdb;

    @RequestMapping(value = "/rest/userslist", method = RequestMethod.GET, produces="application/json")
    public @ResponseBody
    List<ClientAccount> getUsersList(HttpServletRequest request) throws JSONException, IOException {
        int pageNumber;
        try {
            pageNumber = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e){
            pageNumber = 1;
        }
        return accountDAOdb.listClientAccounts(pageNumber);
    }

    @RequestMapping(value = "/rest/userslist/pagination", method = RequestMethod.GET)
    public @ResponseBody
    PaginationInfo pagination(){
        int pages = (int)Math.ceil(accountDAOdb.getAccountsCount() / 10.0);
        if(this.pagination == null){
            this.pagination = new PaginationInfo(pages, 1, Math.min(pages, 7), url);
        }
        this.pagination.setPagesForView(Math.min(pages, 7));
        this.pagination.setPagesCount(pages);
        return this.pagination;
    }

    @RequestMapping(value = "/rest/userslist/pagination", method = RequestMethod.POST)
    public @ResponseBody void paginationPost(@RequestBody PaginationInfo pagination){
        this.pagination = pagination;
    }

    @RequestMapping(value = "/rest/userslist", method = {RequestMethod.POST, RequestMethod.PUT})
    public @ResponseBody void updateAccountStatus(@RequestBody Account account){
        accountDAOdb.updateAccountStatus(account);
    }

    @RequestMapping(value = "/rest/userslist/transaction", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Transaction> getUserTransactions(HttpServletRequest request){
        int accountNumber = Integer.parseInt(request.getParameter("accountNumber"));
        List<Transaction> result = transactionDAOdb.userTransactionList(accountNumber, 5);
        return result;
    }

    private PaginationInfo pagination;

}
