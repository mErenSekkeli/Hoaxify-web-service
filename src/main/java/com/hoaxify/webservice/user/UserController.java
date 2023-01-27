package com.hoaxify.webservice.user;

import com.hoaxify.webservice.error.ApiError;
import com.hoaxify.webservice.shared.GenericResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired//otomatik olarak instance set ediyor
    UserService userService;

    @PostMapping("/api/1.0/users")
    @ResponseStatus(HttpStatus.CREATED)//response tipini değiştirir. Opsiyoneldir.
    public ResponseEntity<?> createUser(@Valid/*validation logicten sonra bu fonksiyona giriyor*/ @RequestBody Users user){
        userService.saveUser(user);
        return ResponseEntity.ok(new GenericResponse("User Created Successfully!"));//shared edilmiş class, JSON değer dönderiyor
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException exception){
        ApiError error = new ApiError(400, "Validation Error", "/api/1.0/users");
        Map<String, String> validationErrors = new HashMap<>();
        for(FieldError fieldError : exception.getBindingResult().getFieldErrors()){
            //default mesaj gönderir
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        error.setValidationErrors(validationErrors);
        return error;
    }

    @PostMapping("/api/1.0/getUserById")
    public GenericResponse getUser(@RequestParam long id){
        Users user;
        try{
            user = userService.getUserById(id);
            System.out.println(user.toString());
        }catch (NullPointerException e){
            return new GenericResponse("User With id is " + id + " Has Not Found!");
        }
        return new GenericResponse("User Has Brought Successfully!");
    }
}
