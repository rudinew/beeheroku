package com.bee.web.controllers;

import com.bee.backend.domain.data.*;
import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.service.data.*;
import com.bee.backend.service.security.UserService;
import com.bee.web.exceptions.ScansNotFoundException;
import com.bee.web.utils.UserIPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@SessionAttributes("beeDocument")
public class DocController {
    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(DocController.class);

    public static final String DOCS_URL_MAPPING =  "personalDocs/docs";
    public static final String CREATE_OR_UPDATE_DOC_URL_MAPPING = "personalDocs/createOrUpdateDocForm";
    public static final String EMAIL_FORM_URL_MAPPING = "personalDocs/emailForm";

    /*http://stackoverflow.com/questions/16894900/spring-autowiring-service-doesnt-work-in-my-controller*/

    @Resource(name="beeDocumentService")
    private BeeDocumentService beeDocumentService;

    @Autowired
    private BeePersonService beePersonService;

    @Autowired
    private UserService userService;

    @Autowired
    private BeeTypeService beeTypeService;

    @Autowired
    private BeeActionsService beeActionsService;

    @Autowired
    private EmailService emailService;

    /* 14.06.2017 from petclinic*/
    //довідник Типи документів
    @ModelAttribute("docTypes")
    public Collection<BeeDocType> populateBeeDocType() {
        return this.beeTypeService.getBeeDocTypeAll();
    }

    //користувач
    @ModelAttribute("beeUsers")
    public BeeUsers populateCurrentBeeUsers() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        return beeUsers;
    }

    @RequestMapping(value = "/personInfo/{personId}/docs", method = RequestMethod.GET)
    public String viewList(@PathVariable("personId") long personId,
                           Model model,
                           HttpServletRequest request){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeePerson beePerson = beePersonService.getBeePersonByOne(personId);
        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("beePerson", beePerson);
        model.addAttribute("beeDocument", beeDocumentService.getBeeDocumentByBeePerson(beePerson));

        LOG.info("{} got docs page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));
        return DOCS_URL_MAPPING;
    }

    @RequestMapping(value = "/personInfo/{personId}/doc/new", method = RequestMethod.GET)
    public String initCreationForm(@PathVariable("personId") long personId,
                                   Model model,
                                   HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeDocument beeDocument = new BeeDocument();
        BeePerson beePerson = this.beePersonService.getBeePersonByOne(personId);
        beeDocument.setBeePerson(beePerson);

        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("beeDocument",beeDocument);

        LOG.info("{} got card/new page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));
        return CREATE_OR_UPDATE_DOC_URL_MAPPING;
    }

    @RequestMapping(value =  "/personInfo/{personId}/doc/new/{typeId}", method = RequestMethod.GET)
    public String initCreationFormWithType(@PathVariable("personId") long personId,
                                           @PathVariable("typeId") long typeId, ///
                                           Model model,
                                           HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeDocument beeDocument = new BeeDocument();
        BeePerson beePerson = this.beePersonService.getBeePersonByOne(personId);
        beeDocument.setBeePerson(beePerson);
        BeeDocType beeDocType = this.beeTypeService.getBeeDocTypeByOne(typeId);
        if (!beeDocType.equals(null)) {
            beeDocument.setBeeDocType(beeDocType); ///
        }
        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("beeDocument",beeDocument);
        LOG.info("{} got card/new page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));
        return CREATE_OR_UPDATE_DOC_URL_MAPPING;
    }

    @RequestMapping(value = {"/personInfo/{personId}/doc/new",  "/personInfo/{personId}/doc/new/{typeId}"}, method = RequestMethod.POST)
    public String processCreationForm(@ModelAttribute("beeDocument") @Valid BeeDocument beeDocument,
                                      BindingResult result,
                                      @RequestParam("photo") MultipartFile[] photo,
                                      SessionStatus status,
                                      HttpServletRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (result.hasErrors()) {
            LOG.warn("{} got errors - {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    result.toString(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
            return CREATE_OR_UPDATE_DOC_URL_MAPPING;
        }

        try {
            this.beeDocumentService.addBeeDocument(beeDocument, photo, user);
            status.setComplete();
            //Журнал дій
            BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
            String beePersonName = beeDocument.getBeePerson().getName();
            String beeDocTypeName = beeDocument.getBeeDocType().getName();
            String beeDocNum = beeDocument.getSeries() + ' ' + beeDocument.getNum();
            String mes = String.format("вставка документу %s клієнта %s  (userIP = %s , SessionId = %s )",
                    beeDocNum,
                    beePersonName,
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
            BeeActions beeActions = new BeeActions(beeUsers, mes, beeDocTypeName);
            this.beeActionsService.saveAction(beeActions);

            LOG.info("{} inserted document {} for {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    beeDocument.getBeeDocType().getName(),
                    beeDocument.getBeePerson().getName(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
        } catch (Exception e) {
            LOG.error("{} got errors - {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    e.getMessage(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request)
                    );
            return e.getMessage();

        }
        return "redirect:/personInfo/{personId}/docs";
    }

    @RequestMapping(value = "/personInfo/{personId}/doc/{documentId}/edit", method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable("documentId") long documentId,
                                 Model model,
                                 HttpServletRequest request){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeDocument beeDocument = beeDocumentService.getBeeDocumentByOne(documentId);
        List<BeeDocumentFile> beeDocumentFiles = this.beeDocumentService.getBeeDocumentFileByBeeDocument(beeDocument);

        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("beeDocument", beeDocument);
        model.addAttribute("photo", beeDocumentFiles);

        LOG.info("{} got doc/edit page (userIP = {}, SessionId = {})",
                user.getUsername(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));

        return CREATE_OR_UPDATE_DOC_URL_MAPPING;
    }

    @RequestMapping(value = "/personInfo/{personId}/doc/{documentId}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public String processUpdateForm(@ModelAttribute("beeDocument") @Valid BeeDocument beeDocument,
                                    BindingResult result,
                                    @RequestParam("photo") MultipartFile[] photo,
                                    SessionStatus status,
                                    HttpServletRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (result.hasErrors()) {
            LOG.warn("{} got errors - {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    result.toString(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request) );
            return CREATE_OR_UPDATE_DOC_URL_MAPPING;
        }

        try {
            this.beeDocumentService.addBeeDocument(beeDocument, photo, user);
            status.setComplete();
            //Журнал дій
            BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
            String beePersonName = beeDocument.getBeePerson().getName();
            String beeDocTypeName = beeDocument.getBeeDocType().getName();
            String beeDocNum = beeDocument.getSeries() + ' ' + beeDocument.getNum();
            String mes = String.format("редагування документу %s клієнта %s  (userIP = %s , SessionId = %s )",
                    beeDocNum,
                    beePersonName,
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
            BeeActions beeActions = new BeeActions(beeUsers, mes, beeDocTypeName);
            this.beeActionsService.saveAction(beeActions);

            LOG.info("{} updated document {} for {} doc/edit page (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    beeDocument.getBeeDocType().getName(),
                    beeDocument.getBeePerson().getName(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request));
        } catch (Exception e) {
            LOG.error("{} got errors - {} (userIP = {}, SessionId = {})",
                    user.getUsername(),
                    e.getMessage(),
                    UserIPUtils.getClientIp(request),
                    WebUtils.getSessionId(request)
                   );
            return e.getMessage();

        }
        return "redirect:/personInfo/{personId}/docs";
    }

    /**
     * удаление документа
     * @param documentId
     * @return
     */
    @RequestMapping(value = "/personInfo/{personId}/doc/{documentId}/delete", method = RequestMethod.GET)
    public String deleteForm(@PathVariable("documentId") Long documentId, HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeDocument beeDocument = this.beeDocumentService.getBeeDocumentByOne(documentId);
        String docName = "";
        String beeDocTypeName = "";
        if (beeDocument.getBeeDocType() != null ) {
            docName = beeDocument.getBeeDocType().getName() + " " + beeDocument.getNum() + " " + beeDocument.getSeries();
            beeDocTypeName = beeDocument.getBeeDocType().getName();
        }

        this.beeDocumentService.deleteBeeDocument(documentId);

        //Журнал дій
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        String beePersonName = beeDocument.getBeePerson().getName();
        String beeDocNum = beeDocument.getSeries() + ' ' + beeDocument.getNum();
        String mes = String.format("видалено документ %s клієнта %s  (userIP = %s , SessionId = %s )",
                beeDocNum,
                beePersonName,
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));
        BeeActions beeActions = new BeeActions(beeUsers, mes, beeDocTypeName);
        this.beeActionsService.saveAction(beeActions);

        LOG.info("{} deleted {} (userIP = {}, SessionId = {})",
                user.getUsername(),
                docName,
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));

        return "redirect:/personInfo/{personId}/docs";
    }

    /**
     * скачивание файлов
     * @param photoId
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/personInfo/{personId}/doc/{documentId}/download/{photoId}", method = RequestMethod.GET)
    public void downloadFile(@PathVariable Long photoId, HttpServletResponse response) throws IOException {
        BeeDocumentFile beeDocumentFile = this.beeDocumentService.getBeeDocumentFileByOne(photoId);
        response.setContentType(beeDocumentFile.getType());
        response.setContentLength(beeDocumentFile.getFile().length);
        //   LOG.info(response.getCharacterEncoding());
        //    LOG.info(beeTaxPhoto.getPhotoName());
        // response.setHeader("Content-Disposition","attachment; filename=\"" + "ту-ту" +"\"");
      /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
        //   response.setHeader("Content-Disposition", String.format("inline; filename=\"" + beeTaxPhoto.getPhotoName()  +"\""));
        response.setHeader("Content-Disposition","attachment; filename=\"" + beeDocumentFile.getName() +"\"");
        //  LOG.info("before download file");
        //Spring FileCopyUtils utility class to copy stream from source to destination
        // FileCopyUtils.copy(beeTaxPhoto.getPhoto(), response.getOutputStream());
        ServletOutputStream servletOutputStream = response.getOutputStream();
        FileCopyUtils.copy(beeDocumentFile.getFile(), servletOutputStream);
        //нужно ли flush() и  close()?
        servletOutputStream.flush();
        servletOutputStream.close();
        LOG.info("download file");
    }

    /**
     * удаление файлов
     * @param photoId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/personInfo/{personId}/doc/{documentId}/delete/{photoId}", method = RequestMethod.GET)
    public String deleteFile(@PathVariable Long photoId) throws IOException {
        this.beeDocumentService.deleteBeeDocumentFile(photoId);
        LOG.info("after delete file");
        return "redirect:/personInfo/{personId}/doc/{documentId}/edit";
    }



    /**
     * view picture
     * @param photoId
     * @return
     * @throws IOException
     */
    // http://www.baeldung.com/spring-requestmapping

    @RequestMapping(value =   "/personInfo/{personId}/doc/{photoId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> viewUploadedFiles(@PathVariable("photoId") Long photoId) throws IOException {

        return photoById(photoId);
    }
    private ResponseEntity<byte[]> photoById(long id) {
        BeeDocumentFile beeDocumentFile = this.beeDocumentService.getBeeDocumentFileByOne(id);
        byte[] bytes = beeDocumentFile.getFile();
       /* if (bytes == null)
            throw new PhotoNotFoundException();*/
        if (bytes == null)
            return null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(beeDocumentFile.getType()));
        headers.add("content-disposition", "inline;filename=" + beeDocumentFile.getName());
        // headers.setContentType(MediaType.parseMediaType(beeTaxPhoto.getPhotoType()));
        //headers.add("content-disposition", "inline;filename=" + beeTaxPhoto.getPhotoName());
        //headers.setContentType(MediaType.parseMediaType(beeTaxPhoto.getPhotoType()));
        //headers.setContentType(MediaType.APPLICATION_ATOM_XML);
        //headers.setContentType(MediaType.IMAGE_PNG);
        //ALL - nothing
        //MULTIPART_FORM_DATA - view jpg
        //MediaType.valueOf(MediaType.ALL_VALUE) nothing
        //APPLICATION_OCTET_STREAM - view jpg

        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }

    ////email
    @RequestMapping(value = "/personInfo/{personId}/doc/{documentId}/sendEmail", method = RequestMethod.GET)
    public String sendEmailGet(@PathVariable("documentId") Long documentId, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeDocument beeDocument = this.beeDocumentService.getBeeDocumentByOne(documentId);

        if (beeDocument.getNrOfBeeDocumentFiles() == 0) {
            throw new ScansNotFoundException();
        }

        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("beePerson", beeDocument.getBeePerson());
        model.addAttribute("beeDocument", beeDocument);

        return EMAIL_FORM_URL_MAPPING;
    }

    @RequestMapping(value = "/personInfo/{personId}/doc/{documentId}/sendEmail", method = RequestMethod.POST)
    public String sendEmailPost(@PathVariable("documentId") Long documentId, HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BeeDocument beeDocument = this.beeDocumentService.getBeeDocumentByOne(documentId);
        String docName = beeDocument.getBeeDocType().getName() +" " + beeDocument.getNum() + " " + beeDocument.getSeries();
        String emailTo =  request.getParameter("emailTo");
        String emailSubject = request.getParameter("subject");
        String emailMessage = request.getParameter("message");

        if (beeDocument.getNrOfBeeDocumentFiles() == 0) {
            throw new ScansNotFoundException();
        }
        List<BeeDocumentFile> beeDocumentFiles = beeDocument.getBeeDocumentFiles();
        List<ByteArrayResource> attaches = new ArrayList<>();
        StringBuffer attachFileNames = new StringBuffer();

        for(int i = 0; i < beeDocumentFiles.size() ;i++){
            //https://stackoverflow.com/questions/28246736/spring-rest-posting-files
            final int finalI = i;
            ByteArrayResource attach = new ByteArrayResource(beeDocumentFiles.get(finalI).getFile()){
                @Override
                public String getFilename(){
                    return beeDocumentFiles.get(finalI).getName();
                }
            };

            attaches.add(attach);
            attachFileNames.append(attach.getFilename() + "; ");
        }

        emailService.sendMessageWithAttachment(emailTo, emailSubject, emailMessage, attaches);

        //Журнал дій
        BeeUsers beeUsers = userService.getUserByLogin(user.getUsername());
        String beePersonName = beeDocument.getBeePerson().getName();
        String beeDocTypeName = beeDocument.getBeeDocType().getName();
        String beeDocNum = beeDocument.getSeries() + ' ' + beeDocument.getNum();
        String mes = String.format("надіслано на пошту %s документ %s з файлами %s клієнта %s (userIP = %s , SessionId = %s )",
                emailTo,
                beeDocNum,
                attachFileNames.toString(),
                beePersonName,
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));
        BeeActions beeActions = new BeeActions(beeUsers, mes, beeDocTypeName);
        this.beeActionsService.saveAction(beeActions);

        LOG.info("{} sended email {} with files {}  (userIP = {}, SessionId = {})",
                user.getUsername(),
                docName,
                attachFileNames.toString(),
                UserIPUtils.getClientIp(request),
                WebUtils.getSessionId(request));

        return "redirect:/personInfo/{personId}/docs";
    }



}
