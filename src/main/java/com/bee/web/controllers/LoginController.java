package com.bee.web.controllers;

import com.bee.web.utils.UserIPUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {
    /** The application logger */
  /*  private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);*/

    @RequestMapping
    public String loginPage(HttpServletRequest request) {
        //for test
        System.out.println(String.format("return login page (userIP = %s, SessionId = %s)",
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request)));
      /*  LOG.info("return login page (userIP = {}, SessionId = {})",
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));*/

        return "user/login";
    }
}