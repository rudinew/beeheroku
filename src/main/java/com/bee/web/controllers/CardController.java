package com.bee.web.controllers;

import com.bee.backend.domain.data.BeeCitizenship;
import com.bee.backend.domain.data.BeePerson;

import com.bee.backend.domain.security.BeeAccessPerson;
import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.domain.security.BeeUsersRelation;
import com.bee.backend.domain.security.UserPrivileges;
import com.bee.backend.service.data.BeePersonService;
import com.bee.backend.service.data.BeeTypeService;

import com.bee.backend.service.security.BeeAccessPersonService;
import com.bee.backend.service.security.UserService;
import com.bee.backend.service.security.UsersRelationService;
import com.bee.web.exceptions.PersonAccessForbiddenException;
import com.bee.web.utils.UserIPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@SessionAttributes("beePerson")  ///post сработал иначе пусто
public class CardController {
    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(CardController.class);

    public static final String CARDS_URL_MAPPING = "personal/cards";
    public static final String CREATE_OR_UPDATE_CARD_URL_MAPPING = "personal/createOrUpdateCardForm";
    public static final String CREATE_OR_UPDATE_CARD_ACCESS_URL_MAPPING = "personal/createOrUpdateCardAccessForm";

    public static final String CARD_ACTION_NAME = "Особова картка";
    public static final String CARD_ACCESS_ACTION_NAME = "Доступ до карти";

    /*http://stackoverflow.com/questions/16894900/spring-autowiring-service-doesnt-work-in-my-controller*/
    @Resource(name="beePersonService")
    private BeePersonService beePersonService;
    @Autowired
    private BeeTypeService beeTypeService;
    @Autowired
    private UserService userService;

    @Autowired
    private UsersRelationService usersRelationService;

    @Autowired
    private BeeAccessPersonService beeAccessPersonService;

    /* 14.06.2017 from petclinic*/
    //довідник Громадянство
    @ModelAttribute("citizen")
    public Collection<BeeCitizenship> populateBeeCitizenship() {
        return this.beeTypeService.getBeeCitizenshipAll();
    }
     //группа контактів користувача
    @ModelAttribute("groupUsers")
    public Collection<BeeUsers> populateBeeUsers() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());

        List<BeeUsersRelation> beeUsersRelations = this.usersRelationService.findByBeeParentUsers(beeUsers);
        if (beeUsersRelations.size() == 0) { return null;}

        Collection<BeeUsers> groupUsers = beeUsersRelations.stream().map(BeeUsersRelation::getBeeChildUsers).collect(Collectors.toCollection(ArrayList::new));
        return groupUsers;

    }

    //користувач
    @ModelAttribute("beeUsers")
    public BeeUsers populateCurrentBeeUsers() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        return beeUsers;
    }
    ///////////////////////////////////

    @RequestMapping("/cards")
    public String initCards(Model model, HttpServletRequest request){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        List<BeeAccessPerson> beeAccessPerson = beeAccessPersonService.getBeeAccessPersonByBeeUsers(beeUsers);
        model.addAttribute("personlist", beePersonService.getBeePersonByBeeAccessPersonsOrderByLastnameAsc(beeAccessPerson));
      //  model.addAttribute("personlist", beePersonService.getBeePersonAll());
        LOG.info("{} got cards page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));

        return CARDS_URL_MAPPING;
    }

    @RequestMapping(value = "/card/new", method = RequestMethod.GET)
    public String initCreationForm(Model model, HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeePerson beePerson = new BeePerson();

        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("beePerson",beePerson);

        LOG.info("{} got card/new page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));
        return CREATE_OR_UPDATE_CARD_URL_MAPPING;
    }

    @RequestMapping(value = "/card/new", method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("beePerson") @Valid BeePerson beePerson,
                                       BindingResult result,
                                       SessionStatus status,
                                      @RequestParam("photo") MultipartFile photo,
                                      HttpServletRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());

        if (result.hasErrors()) {
            LOG.warn("{} had errors - {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    result.toString(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request)
                   );
            return CREATE_OR_UPDATE_CARD_URL_MAPPING;
        }

        try {

            this.beePersonService.addBeePerson(beePerson,photo,user,"insert");
           // this.beePersonService.saveAndFlushBeePerson(beePerson);
             //
            status.setComplete();
            //Журнал дій
            String mes = String.format("вставка нового клієнта %s  (userIP = %s , SessionId = %s )",
                    beePerson.getName(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
         /*   BeeActions beeActions = new BeeActions(beeUsers, mes, CARD_ACTION_NAME);
            this.beeActionsService.saveAction(beeActions);*/

            LOG.info("{} inserted new client {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    beePerson.getName(),
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
        return "redirect:/cards";
    }

    @RequestMapping(value = "/card/{personId}/edit", method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable("personId") long personId, Model model, HttpServletRequest request){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        List<BeeAccessPerson> beeAccessPerson = beeAccessPersonService.getBeeAccessPersonByBeeUsers(beeUsers);
        BeePerson beePerson =  beePersonService.getBeePersonByOneAndBeeAccessPersonsIn(personId, beeAccessPerson);
        //https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
        if (beePerson == null) throw new PersonAccessForbiddenException();
       // List<BeePersonFile> beePersonFile = this.beePersonService.getBeePersonFileByBeePerson(beePerson);

        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("beePerson", beePerson);
      //  model.addAttribute("photo", beePersonFile);
        LOG.info("{} got card/edit page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));  ///when debug or info 

        return CREATE_OR_UPDATE_CARD_URL_MAPPING;
    }

    @RequestMapping(value = "/card/{personId}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public String processUpdateForm(@ModelAttribute("beePerson") @Valid BeePerson beePerson,
                                    BindingResult result,
                                    SessionStatus status,
                                    @RequestParam("photo") MultipartFile photo,
                                    HttpServletRequest request) {


        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        if (result.hasErrors()) {
            LOG.warn("{} had errors - {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    result.toString(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
            return  CREATE_OR_UPDATE_CARD_URL_MAPPING;
        }

        try {
            //this.beePersonService.saveBeePerson(beePerson);
            //saveDocumentAndFile(beePerson, photo, user);
            this.beePersonService.addBeePerson(beePerson, photo, user, "update");
            status.setComplete();

            //Журнал дій
            String mes = String.format("редагування клієнта %s  (userIP = %s , SessionId = %s )",
                    user.getUsername(),
                    beePerson.getName(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
           // BeeActions beeActions = new BeeActions(beeUsers, mes, CARD_ACTION_NAME);
         //   this.beeActionsService.saveAction(beeActions);


            LOG.info("{} updated client {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    beePerson.getName(),
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
        return "redirect:/personInfo/{personId}/docs"; /*"/card/{personId}";*/

    }

    /**
     * удаление всей инфы по клиенту
     *
     * @param personId
     * @return
     */
    @RequestMapping(value = "/card/{personId}/delete", method = RequestMethod.GET)
    public String deleteForm(@PathVariable("personId") Long personId, HttpServletRequest request) {
        BeePerson beePerson = beePersonService.getBeePersonByOne(personId);
        String beePersonName = beePerson.getName();

        this.beePersonService.deleteBeePerson(personId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());

        //Журнал дій
        String mes = String.format("видалено клієнта %s  (userIP = %s , SessionId = %s )",
                user.getUsername(),
                beePerson.getName(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));
       // BeeActions beeActions = new BeeActions(beeUsers, mes, CARD_ACTION_NAME);
       // this.beeActionsService.saveAction(beeActions);

        LOG.info("{} deleted client - {} (userIP = {}, SessionId = {})",
                user.getUsername(),
                beePersonName,
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));  ///when debug or info

        return "redirect:/cards";
    }


    ///доступ к карточке
    @RequestMapping(value = "/card/{personId}/access", method = RequestMethod.GET)
    public String initAccessForm(@PathVariable("personId") long personId, Model model, HttpServletRequest request){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        List<BeeAccessPerson> beeAccessPersons = beeAccessPersonService.getBeeAccessPersonByBeeUsers(beeUsers);
        BeePerson beePerson = beePersonService.getBeePersonByOneAndBeeAccessPersonsIn(personId, beeAccessPersons);
        //https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
        if (beePerson == null) throw new PersonAccessForbiddenException();

        BeeAccessPerson beeAccessPerson = new BeeAccessPerson();
        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("beePerson", beePerson);
        model.addAttribute("beeAccessPerson", beeAccessPerson);

        LOG.info("{} got card/access page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));

        return CREATE_OR_UPDATE_CARD_ACCESS_URL_MAPPING;
    }

    ///доступ к карточке
   /* @RequestMapping(value = "/card/{personId}/access", method = RequestMethod.POST)
    public String saveAccessForm(@PathVariable("personId") long personId,
                                 @ModelAttribute("beeAccessPerson") @Valid BeeAccessPerson beeAccessPerson,
                                 BindingResult result,
                                 SessionStatus status,
                                 HttpServletRequest request)
     {
         if (result.hasErrors()) {
             return CREATE_OR_UPDATE_CARD_ACCESS_URL_MAPPING;
         }
         BeePerson beePerson = beePersonService.getBeePersonByOne(personId);
         beeAccessPerson.setBeePerson(beePerson);
         beeAccessPerson.setPrivileges(UserPrivileges.EDIT);
         beeAccessPersonService.saveAndFlushBeeAccessPerson(beeAccessPerson);
         status.setComplete();

         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
         //Журнал дій
         String mes = String.format("надано доступ до клієнта %s користувачу  %s (userIP = %s , SessionId = %s )",
                 beePerson.getName(),
                 beeAccessPerson.getBeeUsers().getLogin(),
                 UserIPUtils.getClientIp(request),
                 WebUtils.getSessionId(request));
        // BeeActions beeActions = new BeeActions(beeUsers, mes, CARD_ACCESS_ACTION_NAME);
       //  this.beeActionsService.saveAction(beeActions);

         return "redirect:/card/{personId}/access";

   }*/


    /**
     * удаление доступа к карточке
     *
     * @param personId
     * @param personAccessId
     * @return
     */
    @RequestMapping(value = "/card/{personId}/access/{personAccessId}/delete", method = RequestMethod.GET)
    public String deleteForm(@PathVariable("personId") Long personId,
                             @PathVariable("personAccessId") Long personAccessId,
                             HttpServletRequest request) {

        BeeAccessPerson beeAccessPerson = this.beeAccessPersonService.getBeeAccessPersonByOne(personAccessId);
        String beePersonName = beeAccessPerson.getBeePerson().getName();
        String beeUserName = beeAccessPerson.getBeeUsers().getLogin();

        this.beeAccessPersonService.deleteBeeAccessPerson(personAccessId);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        //Журнал дій
      /*  String mes = String.format("Закрито доступ до клієнта %s користувачу  %s (userIP = %s , SessionId = %s )",
                beePersonName,
                beeUserName,
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));
        BeeActions beeActions = new BeeActions(beeUsers, mes, CARD_ACCESS_ACTION_NAME);
        this.beeActionsService.saveAction(beeActions);
        */

        LOG.info("{} has blocked {} access to {}'s card (userIP = {}, SessionId = {})",
                user.getUsername(),
                beeUserName,
                beePersonName,
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));  ///when debug or info

        return "redirect:/card/{personId}/access";
    }



    /**
     * view picture
     * @param photoId
     * @return
     * @throws IOException
     */
    // http://www.baeldung.com/spring-requestmapping
/*
    @RequestMapping(value =  {"/card/{personId}/{photoId}", "/card/{personId}/photo/{photoId}"}, method = RequestMethod.GET)
    public ResponseEntity<byte[]> viewUploadedFiles(@PathVariable("photoId") Long photoId) throws IOException {

        return photoById(photoId);
    }
    private ResponseEntity<byte[]> photoById(long id) {
        BeePersonFile beePersonFile = this.beePersonService.getBeePersonFileByOne(id);
        byte[] bytes = beePersonFile.getFile();

        if (bytes == null)
            return null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(beePersonFile.getType()));
        headers.add("content-disposition", "inline;filename=" + beePersonFile.getName());


        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }
*/

}
