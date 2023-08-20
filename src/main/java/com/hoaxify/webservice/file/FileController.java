package com.hoaxify.webservice.file;

import com.hoaxify.webservice.error.ApiError;
import com.hoaxify.webservice.shared.CurrentUser;
import com.hoaxify.webservice.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${hoaxify.api.path}")
public class FileController {

    @Autowired
    FileService fileService;

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

    @PostMapping("/hoax-attachments/{username}")
    @PreAuthorize("#username == #loggedInUser.userName")
    public ResponseEntity<?> saveHoaxAttachment(MultipartFile file, @PathVariable String username, @CurrentUser Users loggedInUser){
        FileAttachment fileAttachment = fileService.saveHoaxAttachment(file);
        if(fileAttachment != null) {
            return ResponseEntity.ok(fileAttachment);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/hoax-attachments/{username}")
    @PreAuthorize("#username == #loggedInUser.userName")
    public ResponseEntity<?> deleteHoaxAttachment(@RequestBody FileAttachment file, @PathVariable String username, @CurrentUser Users loggedInUser){
        return (fileService.cancelFileAttachment(file)) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
