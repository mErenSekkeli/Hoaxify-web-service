package com.hoaxify.webservice.auth;

import com.hoaxify.webservice.error.ApiError;
import com.hoaxify.webservice.user.UserRepository;
import com.hoaxify.webservice.user.UserService;
import com.hoaxify.webservice.user.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    UserService userService;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/api/1.0/auth")
    public ResponseEntity<?> handleAuth(@RequestHeader(name = "Authorization", required = false) String authorization) {

        if(authorization == null || authorization.equals("Basic Og==")) {
            ApiError error = new ApiError(401, "Unauthorized Request", "/api/1.0/auth");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        String base64encoded = authorization.split("Basic ")[1];
        String decoded = new String(Base64.getDecoder().decode(base64encoded));
        String[] parts = decoded.split(":");
        String username = parts[0];
        String password = parts[1];
        //System.out.println("User Name: " + username + " Password: " + password);
        Users inDB = userService.getUserByUserName(username);
        if(inDB == null) {
            throwAuthError(401, "Unauthorized Request", "/api/1.0/auth");
        }
        String hashedPass = inDB.getPass();
        if(!passwordEncoder.matches(password, hashedPass)){
            throwAuthError(401, "Unauthorized Request", "/api/1.0/auth");
        }

        //name, surname, image
        Map<String, String> responseBody = new HashMap<>();

        responseBody.put("name", inDB.getName());
        responseBody.put("surname", inDB.getSurname());
        responseBody.put("image", inDB.getImage());

        return ResponseEntity.ok().body(responseBody);
    }

    public static ResponseEntity<?> throwAuthError(int status, String msg, String path){
        ApiError error = new ApiError(status, msg, path);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

}
