package com.hoaxify.webservice.like;

import com.hoaxify.webservice.error.ApiError;
import com.hoaxify.webservice.like.vm.LikeSubmitVM;
import com.hoaxify.webservice.like.vm.LikeVM;
import com.hoaxify.webservice.shared.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${hoaxify.api.path}")
public class LikeController {

    @Autowired
    LikeService likeService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException exception){
        ApiError error = new ApiError(400, "Validation Error", "/api/1.0/hoaxes");
        Map<String, String> validationErrors = new HashMap<>();
        for(FieldError fieldError : exception.getBindingResult().getFieldErrors()){
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        error.setValidationErrors(validationErrors);
        return error;
    }

    @PostMapping("/hoaxes/likes/{id:[0-9]+}")
    public GenericResponse likeHoax(@PathVariable long id, @RequestBody LikeSubmitVM like){
        likeService.like(like);
        return new GenericResponse("Hoax liked");
    }

    @DeleteMapping("/hoaxes/likes/{id:[0-9]+}/{username}")
    public GenericResponse unlikeHoax(@PathVariable long id, @PathVariable String username){
        likeService.unlike(id, username);
        return new GenericResponse("Like removed");
    }

    @GetMapping("/users/{username}/likes")
    public LikeVM getUsersLikes(@PathVariable String username){
        return likeService.getUsersLikes(username);
    }

}
