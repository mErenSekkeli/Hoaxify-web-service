package com.hoaxify.webservice.auth;


import com.hoaxify.webservice.user.vm.UserVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${hoaxify.api.path}")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/auth")
    public AuthResponse handleAuth(@RequestBody Credentials credentials){
        return authService.authenticate(credentials);
    }


}
