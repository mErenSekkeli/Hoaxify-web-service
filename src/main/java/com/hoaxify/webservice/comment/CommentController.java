package com.hoaxify.webservice.comment;

import com.hoaxify.webservice.comment.vm.CommentSubmitVM;
import com.hoaxify.webservice.comment.vm.CommentVM;
import com.hoaxify.webservice.error.ApiError;
import com.hoaxify.webservice.shared.CurrentUser;
import com.hoaxify.webservice.shared.GenericResponse;
import com.hoaxify.webservice.user.Users;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${hoaxify.api.path}")
public class CommentController {

    @Autowired
    CommentService commentService;

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

    @PostMapping("/hoaxes/{id:[0-9]+}/comments")
    public GenericResponse saveComment(@PathVariable long id, @Valid @RequestBody CommentSubmitVM comment){
        commentService.save(comment, id);
        return new GenericResponse("Comment saved");
    }

    @GetMapping({"/hoaxes/{id:[0-9]+}/comments", "/hoaxes/{id:[0-9]+}/comments/{commentId:[0-9]+}"})
    public Page<CommentVM> getComments(@PathVariable long id, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable page,
                                       @PathVariable(required = false) Long commentId,
                                       @RequestParam(name = "direction", defaultValue = "before") String direction){
        if(commentId != null){
            return commentService.getOldComments(id, commentId, page).map(CommentVM::new);
        }
        return commentService.getCommentsOfHoax(id, page).map(CommentVM::new);
    }

    @DeleteMapping("/comments/{id:[0-9]+}")
    @PreAuthorize("@CommentSecurity.isAllowedToDelete(#id, #loggedInUser)")
    public GenericResponse deleteComment(@PathVariable long id, @CurrentUser Users loggedInUser){
        commentService.deleteComment(id);
        return new GenericResponse("Comment removed");
    }

}
