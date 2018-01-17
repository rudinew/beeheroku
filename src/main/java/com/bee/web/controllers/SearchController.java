package com.bee.web.controllers;

import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.security.BeeAccessPerson;
import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.service.criteria.BeeDocumentSearchService;
import com.bee.backend.service.criteria.BeePersonSearchService;
import com.bee.backend.service.data.BeePersonService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SearchController {
    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BeePersonService beePersonService;

    @Autowired
    private BeeAccessPersonService beeAccessPersonService;

    @Autowired
    private BeePersonSearchService beePersonSearchService;

    @Autowired
    private BeeDocumentSearchService beeDocumentSearchService;

    //користувач
    @ModelAttribute("beeUsers")
    public BeeUsers populateCurrentBeeUsers() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        return beeUsers;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@RequestParam String pattern, Model model, HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        List<BeeAccessPerson> beeAccessPerson = beeAccessPersonService.getBeeAccessPersonByBeeUsers(beeUsers);

        model.addAttribute("personlist",beePersonSearchService.findBySearchTerm(pattern, beeAccessPerson));

        List<BeePerson> beePersons = beePersonService.getBeePersonByBeeAccessPersonsOrderByLastnameAsc(beeAccessPerson);
        model.addAttribute("doclist",beeDocumentSearchService.findBySearchTerm(pattern, beePersons));
/*Response Status: 500 (Parameter value [eee] did not match expected type [org.joda.time.LocalDate (n/a)];
nested exception is java.lang.IllegalArgumentException: Parameter value [eee] did not match expected type [org.joda.time.LocalDate (n/a)])*/
        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());

        model.addAttribute("pattern", pattern);
        LOG.info("{} got search result page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));

        return "searching/results";
    }
}
