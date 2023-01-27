package com.hoaxify.webservice.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.jfr.DataAmount;

import java.util.Date;
import java.util.Map;
@JsonInclude(JsonInclude.Include.NON_NULL)//Sadece Null olmayanları döndürür
public class ApiError {

    private int status;
    private String message;
    private String path;
    private long timeStamp = new Date().getTime();
    private Map<String, String> validationErrors;

    public ApiError(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
