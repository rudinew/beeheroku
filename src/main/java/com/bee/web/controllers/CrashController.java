package com.bee.web.controllers;


import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


//РАЗОБРАТЬ
    //https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc

//http://www.baeldung.com/exception-handling-for-rest-with-spring
@ControllerAdvice
public class CrashController extends ResponseEntityExceptionHandler {

  /*  @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
    */

    //https://github.com/paulc4/mvc-exceptions/blob/master/src/main/java/demo2/web/GlobalExceptionHandlingControllerAdvice.java

    @ExceptionHandler(value = {Exception.class})  //PersonNotFoundException.class
    public ModelAndView handleError(HttpServletRequest req, Exception exception)
            throws Exception {

        // Rethrow annotated exceptions or they will be processed here instead.
        if (AnnotationUtils.findAnnotation(exception.getClass(),
                ResponseStatus.class) != null)
            throw exception;

        logger.error("Request: " + req.getRequestURI() + " raised " + exception);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("timestamp", new Date().toString());
        mav.addObject("status", 500);

        mav.setViewName("support");
        return mav;
    }
}


/*
* from udemy
*
* @ExceptionHandler({StripeException.class, S3Exception.class})
* public ModelAndView signupException(HttpServletRequest request, Exception exception){
* LOG.error("Request {} raised exception {}", request.getRequestURL(), exception);
* ModelAndView mav = new ModelAndView();
* mav.addObject("exception", exception);
* mav.addObject("timestamp", LocalDate.now(Clock.systemUTC()));
* mav.setViewName(GENERIC_ERROR_VIEW_NAME);
* return mav;
* }*/

/*{
    // The application logger
    private static final Logger LOG = LoggerFactory.getLogger(CrashController.class);

    @RequestMapping(value = "/oups", method = RequestMethod.GET)
    public String triggerException() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LOG.info(user.getUsername() + " got crash page");
        throw new RuntimeException("Expected: controller used to showcase what " +
                "happens when an exception is thrown");
    }
}*/
