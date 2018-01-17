package com.bee.web.controllers;


import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.domain.security.BeeUsersRelation;
import com.bee.backend.service.security.UserService;
import com.bee.backend.service.security.UsersRelationService;
import com.bee.web.utils.UserIPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Controller
@SessionAttributes("beeUsersRelation")  ///post сработал иначе пусто
public class UserController {
    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UsersRelationService usersRelationService;

    //користувач
    @ModelAttribute("beeUsers")
    public BeeUsers populateCurrentBeeUsers() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        return beeUsers;
    }

    @RequestMapping("/users")
    public String index(Model model, HttpServletRequest request){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());

        model.addAttribute("userlist", usersRelationService.findByBeeParentUsers(beeUsers));
        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());

        LOG.info("{} got users page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));
        return "settings/users";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String initCreationForm(Model model, HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        BeeUsersRelation beeUsersRelation = new BeeUsersRelation();
        beeUsersRelation.setBeeParentUsers(beeUsers);

        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("users", userService.findAllByLoginNotInOrderByLoginAsc(user.getUsername()));
        model.addAttribute("beeUsersRelation",beeUsersRelation);

        LOG.info("{} got user/add page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));
        return "settings/createOrUpdateUserRelationForm";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("beeUsersRelation") @Valid BeeUsersRelation beeUsersRelation,
                                      BindingResult result,
                                      SessionStatus status,
                                      HttpServletRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (result.hasErrors()) {
            LOG.warn("{} had errors - {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    result.toString(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request)
            );
            return "settings/createOrUpdateUserRelationForm";
        }

        try {
            ///
           this.usersRelationService.saveAndFlushBeeUsersRelation(beeUsersRelation);

            status.setComplete();
            LOG.info("{} inserted new user in family {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    beeUsersRelation.getBeeChildUsers().getLogin(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
        } catch (Exception e) {
            LOG.error("{} got errors - {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    e.getMessage(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
            return e.getMessage();

        }
        return "redirect:/users";
    }

    /**
     * удаление всей инфы по клиенту
     *
     * @param userRelationId
     * @return
     */
    @RequestMapping(value = "/user/{userRelationId}/delete", method = RequestMethod.GET)
    public String deleteForm(@PathVariable("userRelationId") Long userRelationId, HttpServletRequest request) {
        BeeUsersRelation beeUsersRelation = usersRelationService.getBeeUsersRelationByOne(userRelationId);
        String beePersonLogin = beeUsersRelation.getBeeChildUsers().getLogin();

        this.usersRelationService.deleteBeeUsersRelation(userRelationId);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LOG.info("{} deleted client - {} from own group (userIP = {}, SessionId = {})",
                user.getUsername(),
                beePersonLogin,
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));  ///when debug or info

        return "redirect:/users";
    }


}
