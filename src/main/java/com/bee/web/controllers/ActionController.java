package com.bee.web.controllers;

import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.service.data.BeeActionsService;
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


@Controller
public class ActionController {
    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(ActionController.class);

    //журнал дій
    @Autowired
    private BeeActionsService beeActionsService;
    @Autowired
    private UserService userService;

    //користувач
    @ModelAttribute("beeUsers")
    public BeeUsers populateCurrentBeeUsers() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        return beeUsers;
    }

    @RequestMapping("/actions")
    public String index(Model model, HttpServletRequest request){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers users = userService.getUserByLogin(user.getUsername());
        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("actions", beeActionsService.getActionsByUsersOrderByDtFromAsc(users));

        LOG.info("{} got actions page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));
        return "settings/actions";
    }



}
