package com.hoaxify.webservice.shared;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {FileTypeValidator.class}
)
public @interface FileType {

    String message() default "{hoaxify.constraint.image.fileType.message}";

    String[] types() default {"image/png", "image/jpeg"};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
