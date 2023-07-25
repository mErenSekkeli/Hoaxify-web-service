package com.hoaxify.webservice.auth;

import com.hoaxify.webservice.shared.CurrentUser;
import com.hoaxify.webservice.user.UserService;
import com.hoaxify.webservice.user.vm.UserVM;
import com.hoaxify.webservice.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/api/1.0/auth")
    public UserVM handleAuth(@CurrentUser Users user) {
        return new UserVM(user);
    }


}
