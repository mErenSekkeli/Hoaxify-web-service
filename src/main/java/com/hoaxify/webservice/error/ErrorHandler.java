package com.hoaxify.webservice.error;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ErrorHandler implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;
    private ErrorAttributeOptions errorAttributeOptions = ErrorAttributeOptions.defaults();

    @RequestMapping("/error")
    public ApiError handleError(WebRequest webRequest){
        Map<String, Object> attributes =  this.errorAttributes.getErrorAttributes(webRequest, errorAttributeOptions);
        String msg = (String) attributes.get("error");
        String path = (String) attributes.get("path");
        int status = (int) attributes.get("status");
        ApiError error = new ApiError(status, msg, path);
        return error;
    }

}
