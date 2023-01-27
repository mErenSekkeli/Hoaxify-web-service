package com.hoaxify.webservice.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {UniqueUsernameValidator.class}
)
public @interface UniqueUsername {

        String message() default "This Username Has Been Taken";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

}
