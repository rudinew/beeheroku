package com.bee.web.validators;

import com.bee.backend.domain.security.BeeUsersRelation;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Rudoman on 27.07.2017.
 */
public class UserRelationValidator implements Validator {
    @Override
    public boolean supports(Class clazz) {
        //just validate the BeeUsers instances
        return BeeUsersRelation.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {


        BeeUsersRelation beeUsersRelation = (BeeUsersRelation)target;

        long beeUsersRelationId  = beeUsersRelation.getBeeChildUsers().getId();

        /*
        * how to
        * SQL Error: 1062, SQLState: 23000
2017-07-27 11:40:08.995 ERROR 11224 --- [nio-9090-exec-5] o.h.engine.jdbc.spi.SqlExceptionHelper   : Duplicate entry '1-4' for key 'UC_USERS_RELATION'*/

     /* if (beeUsersRelation.isNew() && beeUsersRelation.getBeeChildUsers().getId(beeUsersRelationId, true) != null) {
            errors.rejectValue("email", "duplicate", "already exists");
        }*/

    }
}
