package com.hoaxify.webservice.auth;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoaxify.webservice.shared.CurrentUser;
import com.hoaxify.webservice.shared.Views;
import com.hoaxify.webservice.user.UserService;
import com.hoaxify.webservice.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/api/1.0/auth")
    @JsonView(Views.Public.class)
    public ResponseEntity<?> handleAuth(@CurrentUser Users user) {
        return ResponseEntity.ok(user);
    }


}
