package com.lingstep.lhs.sys.authentication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);
    @RequestMapping("/portal")
    public String portal(){
        return "index";
    }

}
