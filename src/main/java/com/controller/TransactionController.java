package com.controller;

import com.model.PaginationInfo;
import com.model.Transaction;
import com.model.User;
import com.service.*;
import com.validator.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationEvent;
import java.io.IOException;
import java.util.*;

@RequestMapping(value = "/rest/transaction")
@RestController
public class TransactionController {

    private final String url = "/rest/transaction/pagination";

    @Autowired
    AccountService accountService;

    @Autowired
    PaginationService paginationService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    @Autowired
    TransactionValidator transactionValidator;

    @Autowired
    ValidationService validationService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public
    List<Transaction> getUserTransactions(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber){
        User authenticatedUser = userService.getAuthenticatedUser();
        return transactionService.userTransactionList(authenticatedUser.getAccountId(), pageNumber, pagination.getRowsPerPage());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity transaction(@RequestBody Transaction currentTransaction,
                                                    BindingResult transactionBinding) throws IOException {
        transactionService.fillTransaction(currentTransaction);
        transactionValidator.validate(currentTransaction, transactionBinding);
        if(transactionBinding.hasErrors()){
            return new ResponseEntity<>(transactionValidator.getErrorMessages(transactionBinding), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        TransactionService.TransactionStatus result = transactionService.makeTransaction(currentTransaction);
        if(result == TransactionService.TransactionStatus.SUCCESSFUL){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result.message(), HttpStatus.INTERNAL_SERVER_ERROR);
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

    @RequestMapping(value = "/validation", method = RequestMethod.GET)
    public ResponseEntity formValidation(@RequestParam(value = ValidationService.MONEY_AMOUNT_PARAM, required = false) String moneyAmount,
                                         @RequestParam(value = ValidationService.DEST_ACC_PARAM, required = false) String destAccId){
        ResponseEntity<String> responseEntity = validationService.transactionFieldsValidate(moneyAmount, destAccId);
        return responseEntity;
    }

    private PaginationInfo pagination;

}
