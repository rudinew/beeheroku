package com.bee.web.controllers;

import com.bee.backend.domain.data.*;
import com.bee.backend.domain.security.BeeAccessPerson;
import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.service.data.*;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * Сделать через POJO
 * марко подсмотреть преобразование
 * http://viralpatel.net/blogs/spring-mvc-multi-row-submit-java-list/
 */

@Controller
public class NotificationController {
    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private BeeDocNotificationService beeDocNotificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private BeePersonService beePersonService;

    @Autowired
    private BeeAccessPersonService beeAccessPersonService;

    @Autowired
    private BeeTypeService beeTypeService;

    @Autowired
    private BeeActionsService beeActionsService;

    @Autowired
    private BeePersonDocumentTypeService beePersonDocumentTypeService;

    //користувач
    @ModelAttribute("beeUsers")
    public BeeUsers populateCurrentBeeUsers() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        return beeUsers;
    }

    //довідник Типи документів
    @ModelAttribute("docTypes")
    public Collection<BeeDocType> populateBeeDocType() {
        return this.beeTypeService.getBeeDocTypeAll();
    }

    @RequestMapping("/notifications/{userId}")
    public String index(Model model, HttpServletRequest request){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("beeUsers", beeUsers);
        model.addAttribute("docNotifications", beeDocNotificationService.getBeeDocNotificationByUsers(beeUsers));

        LOG.info("{} got notifications page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));  ///when debug or info
        return "settings/notifications";
    }


    @RequestMapping(value = "/notifications/{userId}", method = {RequestMethod.PUT, RequestMethod.POST})
    public String processUpdateForm(@ModelAttribute("beeUsers") BeeUsers beeUsers,
                                   // BindingResult result,
                                    SessionStatus status,
                                    HttpServletRequest request) {


        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      /*  if (result.hasErrors()) {
            LOG.warn("{} had errors - {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    result.toString(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
            return  "settings/notifications";
        }*/

        try {

            if (beeUsers.getNrOfBeeDocNotifications() > 0) {
                List<BeeDocNotification> beeDocNotifications = beeUsers.getBeeDocNotifications();
                for (BeeDocNotification item : beeDocNotifications) {

                   // System.out.println("id = " + item.getId());
                    this.beeDocNotificationService.saveAndFlushBeeDocNotification(item);
                    //this.beeDocNotificationService.saveBeeDocNotification(item);
                    //якщо не обовязкове до занесення то видаляємо
                    if(item.getIs_required()){
                        //ВСТАВКА
                        List<BeeAccessPerson> beeAccessPersons = beeAccessPersonService.getBeeAccessPersonByBeeUsers(beeUsers);
                        List<BeePerson> beePersons = beePersonService.getBeePersonByBeeAccessPersonsOrderByLastnameAsc(beeAccessPersons);

                        for (BeePerson item2 : beePersons) {
                            List<BeePersonDocumentType> beePersonDocumentTypes = this.beePersonDocumentTypeService.getBeePersonDocumentTypeByBeePersonAndBeeDocType(item2, item.getBeeDocType());
                            if (beePersonDocumentTypes.isEmpty())
                            {
                                BeePersonDocumentType beePersonDocumentType = new BeePersonDocumentType(item2, item.getBeeDocType());
                                this.beePersonDocumentTypeService.saveAndFlushBeePersonDocumentType(beePersonDocumentType);
                            }
                        }

                    }
                    else{
                        //Налаштування
                        //ВИДАЛЕННЯ внесеного типу з таблиці оповіщень BeePersonDocumentType
                        List<BeeAccessPerson> beeAccessPersons = beeAccessPersonService.getBeeAccessPersonByBeeUsers(beeUsers);
                        List<BeePerson> beePersons = beePersonService.getBeePersonByBeeAccessPersonsOrderByLastnameAsc(beeAccessPersons);
                        List<BeePersonDocumentType> beePersonDocumentTypes = beePersonDocumentTypeService.getBeePersonDocumentTypeByBeePersonIn(beePersons);

                        for (BeePersonDocumentType item2 : beePersonDocumentTypes) {
                            if (item.getBeeDocType().getId().equals(item2.getBeeDocType().getId())) {
                                this.beePersonDocumentTypeService.deleteBeePersonDocumentType(item2.getId());
                            }
                        }
                        //

                    }
                }
            }
            status.setComplete();

            //Журнал дій
            String mes = String.format("редагування оповіщень %s (userIP = %s , SessionId = %s )",
                    user.getUsername(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
            BeeActions beeActions = new BeeActions(beeUsers, mes, "Налаштування оповіщень");
            this.beeActionsService.saveAction(beeActions);


            LOG.info("{} updated notifications (userIP = {}, SessionId = {})",
                    user.getUsername(),
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
        return "redirect:/";
    }



}
