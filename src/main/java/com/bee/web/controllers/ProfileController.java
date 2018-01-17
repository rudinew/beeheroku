package com.bee.web.controllers;

import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.service.security.UserService;
import com.bee.web.utils.UserIPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
@SessionAttributes("beeUsers")  ///post сработал иначе пусто
public class ProfileController {
    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);


  @Autowired
  private UserService userService;

    //користувач
    @ModelAttribute("beeUsers")
    public BeeUsers populateCurrentBeeUsers() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        return beeUsers;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String initCreationForm(Model model, HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("beeUsers", beeUsers);
        LOG.info("{} got profile/edit page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));  ///when debug or info
        return "user/createOrUpdateUserForm";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("beeUsers") @Valid BeeUsers beeUsers,
                                      BindingResult result,
                                      SessionStatus status,
                                      HttpServletRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (result.hasErrors()) {
            LOG.warn("{} got errors - {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    result.toString(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
            return "user/createOrUpdateUserForm";
        }

        try {
            this.userService.save(beeUsers);
            status.setComplete();
            LOG.info("{} saved profile page (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));  ///when debug or info
        } catch (Exception e) {
            LOG.error("{} got errors - {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    e.getMessage(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
            return e.getMessage();

        }
        return "redirect:/";
    }

}
