package com.hoaxify.webservice.shared;

import com.hoaxify.webservice.file.FileService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class FileTypeValidator implements ConstraintValidator<FileType, String> {

    @Autowired
    FileService fileService;

    String[] types;

    @Override
    public void initialize(FileType constraintAnnotation) {
        this.types = constraintAnnotation.types();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null || s.isEmpty()){
            return true;
        }
        String fileType = fileService.evaluateFileType(s);
        for (String supportedType : types) {
            if(fileType.contains(supportedType)){
                return true;
            }
        }
        return false;
    }
}
