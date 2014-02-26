package com.controller;

import com.dao.AccountDAOdb;
import com.dao.TransactionDAOdb;
import com.dao.UserDAOdb;
import com.model.Account;
import com.model.PaginationInfo;
import com.model.Transaction;
import com.model.User;
import com.service.AccountService;
import com.service.PaginationService;
import com.service.TransactionService;
import com.service.UserService;
import com.validator.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Controller
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

    @RequestMapping(value = "/rest/transaction", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Transaction> getUserTransactions(HttpServletRequest request){
        User authenticatedUser = userService.getAuthenticatedUser();
        int pageNumber;
        try {
            pageNumber = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e){
            return transactionService.userTransactionList(authenticatedUser.getAccountId(), 1, pagination.getRowsPerPage());
        }
        return transactionService.userTransactionList(authenticatedUser.getAccountId(), pageNumber, pagination.getRowsPerPage());
    }

    @RequestMapping(value = "/rest/transaction", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity transaction(@RequestBody Transaction currentTransaction,
                                                    BindingResult transactionBinding) throws IOException {
        currentTransaction.setSourceAccountId(userService.getAuthenticatedUser().getAccountId());
        currentTransaction.setDate(new Date());
        transactionValidator.validate(currentTransaction, transactionBinding);
        if(transactionBinding.hasErrors()){
            return new ResponseEntity<List<String>>(getErrorMessages(transactionBinding), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        TransactionService.TransactionStatus result = transactionService.makeTransaction(currentTransaction);
        if(result == TransactionService.TransactionStatus.SUCCESSFUL){
            return new ResponseEntity<TransactionService.TransactionStatus>(result, HttpStatus.OK);
        }
        return new ResponseEntity<String>(result.message(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/rest/transaction/pagination", method = RequestMethod.GET)
    public @ResponseBody PaginationInfo pagination(HttpServletRequest request){
        HashMap<String, Integer> paginationParams = paginationService.parsePaginationParams(request);
        int rowsPerPage = paginationParams.get(paginationService.ROWS_PER_PAGE_PARAM);
        int pagesForView = paginationParams.get(paginationService.PAGES_FOR_VIEW_PARAM);
        User authenticatedUser = userService.getAuthenticatedUser();
        if(authenticatedUser == null){
            throw new BadCredentialsException("You are not authenticated");
        }
        pagination = paginationService.updatePagination(pagination, rowsPerPage, pagesForView, url);
        return this.pagination;
    }

    @RequestMapping(value = "/rest/transaction/pagination", method = RequestMethod.POST)
    public @ResponseBody void paginationPost(@RequestBody PaginationInfo pagination){
        this.pagination = pagination;
    }

    @RequestMapping(value = "/rest/transaction/validation", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity formValidation(HttpServletRequest request){
        Map parameters = request.getParameterMap();
        if(parameters.containsKey("moneyAmount")){
            Integer money;
            money = Integer.parseInt(request.getParameter("moneyAmount"));
            Account source = accountService.getAccount(userService.getAuthenticatedUser().getAccountId());
            if(!source.isActive()){
                return new ResponseEntity<String>(
                        TransactionService.TransactionStatus.SOURCE_NOT_ACTIVATED.message(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            else if(source.getMoneyAmount() < money){
                return new ResponseEntity<String>(
                        TransactionService.TransactionStatus.NOT_ENOUGH_MONEY.message(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if(parameters.containsKey("destAcc")){
            Integer destAccId = Integer.parseInt(request.getParameter("destAcc"));
            Account dest = accountService.getAccount(destAccId);
            if(dest != null){
                if(!dest.isActive()){
                    return new ResponseEntity<String>(
                            TransactionService.TransactionStatus.DEST_NOT_ACTIVATED.message(),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<String>(
                        TransactionService.TransactionStatus.DEST_ACCOUNT_NOT_EXISTS.message(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity(TransactionService.TransactionStatus.SUCCESSFUL.message(), HttpStatus.OK);
    }

    private ArrayList<String> getErrorMessages(Errors errors){
        List<ObjectError> objectErrors = errors.getAllErrors();
        ArrayList<String> result = new ArrayList<String>();
        for(ObjectError oe : objectErrors){
            result.add(oe.getDefaultMessage());
        }
        return result;
    }

    private PaginationInfo pagination;

}
