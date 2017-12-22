package com.bee.web.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;


//import com.bee.web.domain.PersonEmtyDocPojo;

/**
 * Created by Rudoman on 28.11.2016.
 */
@Controller
public class IndexController {
    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request){
        //return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";

           return "index";


    }


   /* @RequestMapping("/error")
    public String error(){
        return "error";


    }*/

}
