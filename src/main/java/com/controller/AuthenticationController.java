package com.controller;

import com.model.LoginStatus;
import com.model.User;
import com.security.CustomAuthenticationProvider;
import com.service.UserService;
import com.validator.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    TransactionValidator transactionValidator;

    @RequestMapping(value = "/")
    public String homepage1(){
        return "redirect:/page";
    }

    @RequestMapping(value = "/page")
    public String homepage2(){
        return "page";
    }

    @RequestMapping(value = "/rest/login", method = {RequestMethod.POST, RequestMethod.PUT},
            consumes = "application/json")
    public @ResponseBody
    LoginStatus login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginStatus loginStatus) {

        //Logout
        if(loginStatus.isLoggedIn() ||
                (loginStatus.getUsername().isEmpty() && loginStatus.getPassword().isEmpty())){
            SecurityContextHolder.getContext().setAuthentication(null);
            return new LoginStatus("sign in please", null, false, null);
        }

        String username = loginStatus.getUsername();
        String password = loginStatus.getPassword();
        loginStatus = customAuthenticationProvider.authenticate(username, password);
        if(loginStatus.getUsername() == null || !loginStatus.isLoggedIn()){
            response.setStatus(500);
        }
        return loginStatus;
    }

    @RequestMapping(value = "/rest/login", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody LoginStatus login(){
        String login;
        String role;
        try {
            login = userService.getAuthenticatedUser().getLogin();
            role = userService.getAuthenticatedUser().getAuthority();
        } catch (NullPointerException e){
            login = role = null;
        }
        return new LoginStatus(login, null, login != null, role != null ? User.Role.valueOf(role) : null);
    }

}
