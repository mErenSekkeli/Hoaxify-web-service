package com.hoaxify.webservice.hoax;

import com.hoaxify.webservice.error.ApiError;
import com.hoaxify.webservice.hoax.vm.HoaxUpdateVM;
import com.hoaxify.webservice.shared.CurrentUser;
import com.hoaxify.webservice.shared.GenericResponse;
import com.hoaxify.webservice.user.Users;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/1.0")
public class HoaxController {

    @Autowired
    HoaxService hoaxService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException exception){
        ApiError error = new ApiError(400, "Validation Error", "/api/1.0/hoaxes");
        Map<String, String> validationErrors = new HashMap<>();
        for(FieldError fieldError : exception.getBindingResult().getFieldErrors()){
            //default mesaj gönderir
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        error.setValidationErrors(validationErrors);
        return error;
    }

    @PostMapping("/hoaxes/{username}")
    @PreAuthorize("#username == #loggedInUser.userName")
    public GenericResponse createHoax(@PathVariable String username, @Valid @RequestBody Hoaxes hoax, @CurrentUser Users loggedInUser) {
        hoaxService.saveHoax(hoax);
        return new GenericResponse("Hoax Created Successfully!");
    }
}
