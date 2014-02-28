package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StartPageController {

    @RequestMapping(value = "/")
    public String homepage1(){
        return "/page";
    }

    @RequestMapping(value = "/page")
    public String homepage2(){
        return "page";
    }

}
