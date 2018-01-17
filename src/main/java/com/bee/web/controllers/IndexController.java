package com.bee.web.controllers;


import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.data.BeePersonDocumentType;
import com.bee.backend.domain.security.BeeAccessPerson;
import com.bee.backend.domain.security.BeeUsers;

import com.bee.backend.service.data.BeePersonDocumentTypeService;
import com.bee.backend.service.data.BeePersonService;
import com.bee.backend.service.data.BeeTypeService;
import com.bee.backend.service.security.BeeAccessPersonService;
import com.bee.backend.service.security.UserService;

import com.bee.web.utils.UserIPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;


import javax.servlet.http.HttpServletRequest;
import java.util.List;


//import com.bee.web.domain.PersonEmtyDocPojo;


@Controller
public class IndexController {
    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private BeeAccessPersonService beeAccessPersonService;
    @Autowired
    private BeePersonDocumentTypeService beePersonDocumentTypeService;
    @Autowired
    private BeePersonService beePersonService;
    @Autowired
    private UserService userService;
    //користувач
    @ModelAttribute("beeUsers")
    public BeeUsers populateCurrentBeeUsers() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        return beeUsers;
    }

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        List<BeeAccessPerson> beeAccessPersons = beeAccessPersonService.getBeeAccessPersonByBeeUsers(beeUsers);
        List<BeePerson> beePersons = beePersonService.getBeePersonByBeeAccessPersonsOrderByLastnameAsc(beeAccessPersons);
        List<BeePersonDocumentType> beePersonDocumentTypes = beePersonDocumentTypeService.getBeePersonDocumentTypeFirst5ByBeePersonIn(beePersons);
        Long countBeePersonDocumentTypes =  beePersonDocumentTypeService.countByBeePersonIn(beePersons);

        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("emptyDoc", beePersonDocumentTypes);
        model.addAttribute("countEmptyDoc", countBeePersonDocumentTypes);

        LOG.info("{} got index page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));  ///when debug or info
        return "index";
    }

 /*   @RequestMapping("/")
    public String index(Model model, HttpServletRequest request){
        //return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";

           return "index";


    }
*/


}
