package com.bee.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;


//import com.bee.web.domain.PersonEmtyDocPojo;


@Controller
public class IndexController {


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
