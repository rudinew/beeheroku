package com.bee.web.controllers;


import com.bee.backend.domain.security.BeeUsers;

import com.bee.backend.service.data.BeeTypeService;
import com.bee.backend.service.security.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;



//import com.bee.web.domain.PersonEmtyDocPojo;


@Controller
public class IndexController {

    @Autowired
    private UserService userService;
    @Autowired
    private BeeTypeService beeTypeService;
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
      /* List<BeeAccessPerson> beeAccessPersons = beeAccessPersonService.getBeeAccessPersonByBeeUsers(beeUsers);
        List<BeePerson> beePersons = beePersonService.getBeePersonByBeeAccessPersonsOrderByLastnameAsc(beeAccessPersons);
        List<BeePersonDocumentType> beePersonDocumentTypes = beePersonDocumentTypeService.getBeePersonDocumentTypeFirst5ByBeePersonIn(beePersons);
        Long countBeePersonDocumentTypes =  beePersonDocumentTypeService.countByBeePersonIn(beePersons);
*/
        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("beeCitizenship", beeTypeService.getBeeCitizenshipAll());
      //  model.addAttribute("emptyDoc", beePersonDocumentTypes);
       // model.addAttribute("countEmptyDoc", countBeePersonDocumentTypes);

      /*  LOG.info("{} got index page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request)); */  ///when debug or info
        return "index";
    }

 /*   @RequestMapping("/")
    public String index(Model model, HttpServletRequest request){
        //return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";

           return "index";


    }
*/


}
