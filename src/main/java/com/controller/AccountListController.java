package com.controller;

import com.model.Account;
import com.model.ClientAccount;
import com.model.PaginationInfo;
import com.model.Transaction;
import com.service.AccountService;
import com.service.PaginationService;
import com.service.TransactionService;
import com.service.UserService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/rest/userslist")
@RestController
public class AccountListController {

    private final String url = "/rest/userslist/pagination";

    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PaginationService paginationService;

    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    public
    List<ClientAccount> getUsersList(@RequestParam(value = "page", defaultValue = "1") int pageNumber) throws JSONException, IOException {
        return accountService.listClientAccounts(pageNumber, pagination.getRowsPerPage());
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.GET)
    public PaginationInfo pagination(@RequestParam(value = PaginationService.PAGES_FOR_VIEW_PARAM,
            defaultValue = PaginationService.MAX_PAGES) Integer pagesForView,
                                     @RequestParam(value = PaginationService.ROWS_PER_PAGE_PARAM,
                                             defaultValue = PaginationService.DEFAULT_ROWS_PER_PAGE) Integer rowsPerPage){
        pagination = paginationService.updatePagination(pagination, rowsPerPage, pagesForView, url);
        return pagination;
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.POST)
    public void paginationPost(@RequestBody PaginationInfo pagination){
        this.pagination = pagination;
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Account> updateAccountStatus(@RequestBody Account account){
        try {
            accountService.updateAccountStatus(account);
        } catch (DataAccessException e){
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.GET, produces = "application/json")
    public
    List<Transaction> getUserTransactions(@RequestParam(value = "accountNumber") int accountNumber){
        List<Transaction> result = transactionService.userTransactionListDesc(accountNumber, 5);
        return result;
    }

    private PaginationInfo pagination;

}
