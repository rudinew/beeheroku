package com.bee.web.validators;


import com.bee.backend.domain.security.BeeUsers;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

//@Component
public class UserValidator implements Validator {


    //login is not unique?
    private Boolean isDuplicate = false;

    public void setDuplicate(Boolean duplicate) {
        isDuplicate = duplicate;
    }

    @Override
    public boolean supports(Class clazz) {
        //just validate the BeeUsers instances
        return BeeUsers.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (isDuplicate) {
            errors.rejectValue("login", "duplicate", "already exists");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
                "required.password", "Field name is required.");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword",
                "required.confirmPassword", "Field name is required.");

        BeeUsers beeUser = (BeeUsers)target;
       //raised java.lang.NullPointerException
     /*  if (beeUser != null) {
            if (userService.getUserByLogin(beeUser.getLogin()) != null) {
               errors.rejectValue("login", "Duplicate.login");

            }
        }*/

        if(!(beeUser.getPassword().equals(beeUser.getConfirmPassword()))){
            errors.rejectValue("password", "notmatch.password");
        }




       /*   String login = beeUser.getLogin();

      if (beeUser.isNew() && beeUser.getLogin(login, true) != null) {
            errors.rejectValue("login", "duplicate", "already exists");
        }*/

        /* String name = pet.getName();
        if (!StringUtils.hasLength(name)) {
            errors.rejectValue("name", "required", "required");
        } else if (pet.isNew() && pet.getOwner().getPet(name, true) != null) {
            errors.rejectValue("name", "duplicate", "already exists");
        }*/

    }

}
