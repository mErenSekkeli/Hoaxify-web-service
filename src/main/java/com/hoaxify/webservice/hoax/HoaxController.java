package com.hoaxify.webservice.hoax;

import com.hoaxify.webservice.error.ApiError;
import com.hoaxify.webservice.hoax.vm.HoaxSubmitVM;
import com.hoaxify.webservice.hoax.vm.HoaxVM;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        error.setValidationErrors(validationErrors);
        return error;
    }

    @PostMapping("/hoaxes/{username}")
    @PreAuthorize("#username == #loggedInUser.userName")
    public GenericResponse createHoax(@PathVariable String username, @Valid @RequestBody HoaxSubmitVM hoax, @CurrentUser Users loggedInUser) {
        hoaxService.saveHoax(hoax, loggedInUser);
        return new GenericResponse("Hoax Created Successfully!");
    }

    @GetMapping("/hoaxes")
    public Page<HoaxVM> getHoaxList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable page){
        return hoaxService.getHoaxList(page).map(HoaxVM::new);
    }

    @GetMapping({"/hoaxes/{id:[0-9]+}", "/users/{username}/hoaxes/{id:[0-9]+}"})
    public ResponseEntity<?> getHoaxRelativeList(@PathVariable long id, @PathVariable(required = false) String username,
                                                 @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable page,
                                              @RequestParam(name = "count", required = false, defaultValue = "false") boolean count,
                                                 @RequestParam(name = "direction", defaultValue = "before") String direction){

        if(count) {
            long newHoaxCount = hoaxService.getNewHoaxesCount(id, username);
            Map<String, Long> response = new HashMap<>();
            response.put("count", newHoaxCount);
            return ResponseEntity.ok(response);
        }
        if(direction.equals("after")) {
            List<HoaxVM> newHoaxes = hoaxService.getNewHoaxes(id, username, page.getSort())
                    .stream().map(HoaxVM::new).toList();
            return ResponseEntity.ok(newHoaxes);
        }

        return ResponseEntity.ok(hoaxService.getOldHoaxes(id, username, page).map(HoaxVM::new));
    }

    @GetMapping("/users/{username}/hoaxes")
    public Page<HoaxVM> getHoaxesOfUser(@PathVariable String username, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable page){
        return hoaxService.getHoaxesOfUser(username, page).map(HoaxVM::new);
    }

    @DeleteMapping("/hoaxes/{id:[0-9]+}")
    @PreAuthorize("@hoaxSecurity.isAllowedToDelete(#id, #loggedInUser)")
    public GenericResponse deleteHoax(@PathVariable long id, @CurrentUser Users loggedInUser){
        hoaxService.delete(id);
        return new GenericResponse("Hoax Deleted Successfully!");
    }

}