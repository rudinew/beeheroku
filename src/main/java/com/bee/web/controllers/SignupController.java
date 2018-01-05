package com.bee.web.controllers;

import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.domain.security.UserRole;
import com.bee.backend.service.security.UserService;
import com.bee.web.utils.UserIPUtils;
import com.bee.web.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@SessionAttributes("beeUsers")
public class SignupController {
    @Autowired
    private UserService userService;

    public static final String SIGNUP_URL_MAPPING = "/signup";
    public static final String SIGNUP_SUCCESS_URL_MAPPING = "/signup-success";
    public static final String REGISTRATION_URL_MAPPING = "registration/signup";
    public static final String REGISTRATION_SUCCESS_URL_MAPPING = "registration/signupSuccess";

    @RequestMapping(value = SIGNUP_URL_MAPPING, method = RequestMethod.GET)
    public String signUpGet(Model model, HttpServletRequest request) {
        BeeUsers beeUsers = new BeeUsers();
        model.addAttribute("beeUsers",beeUsers);

        return REGISTRATION_URL_MAPPING;
    }

    @RequestMapping(value = SIGNUP_URL_MAPPING, method = RequestMethod.POST)
    public String signUpPost(@ModelAttribute("beeUsers") @Valid BeeUsers beeUsers,
                             BindingResult result,
                             SessionStatus status,
                             HttpServletRequest request) {

        UserValidator userValidator = new UserValidator();
        BeeUsers beeUsersCreated = this.userService.getUserByLogin(beeUsers.getLogin());
        if (beeUsersCreated != null){  //!beeUsersCreated.getLogin().isEmpty()  if empty table raised java.lang.NullPointerException
            userValidator.setDuplicate(true);
        }

        userValidator.validate(beeUsers, result);
        // new UserValidator().validate(beeUsers, result);
        if (result.hasErrors()) {
            //for test
            System.out.println(String.format("Try to sign up. Have an error -  %s (userIP =  %s, SessionId =  %s)" ,
                    result.toString(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request)));
         /*   LOG.warn("Try to sign up. Have an error - {} (userIP = {}, SessionId = {})",
                    result.toString(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));  */
            return REGISTRATION_URL_MAPPING;
        }

        else {
            beeUsers.setIs_active(true);
            beeUsers.setRole(UserRole.ROLE_CLIENT);

            this.userService.addUser(beeUsers);

            status.setComplete();
            //for test
            System.out.println(String.format("%s - sign up success! (userIP = %s, SessionId = %s)" ,
                    beeUsers.getLogin(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request)));
           /* LOG.info("{} - sign up success! (userIP = {}, SessionId = {})",
                    beeUsers.getLogin(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));*/
            //  return REGISTRATION_SUCCESS_URL_MAPPING;
            return "redirect:/signup-success"; //+SIGNUP_SUCCESS_URL_MAPPING;
        }
    }

    @RequestMapping(value = SIGNUP_SUCCESS_URL_MAPPING, method = RequestMethod.GET)
    public String signUpSuccessGet(Model model, HttpServletRequest request) {
        //for test
        System.out.println(String.format("return SIGNUP_SUCCESS page (userIP = %s, SessionId = %s)" ,
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request)));
        //for test
     /*   LOG.info("return SIGNUP_SUCCESS page (userIP = {}, SessionId = {})",
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));*/

        return REGISTRATION_SUCCESS_URL_MAPPING;
    }
}
