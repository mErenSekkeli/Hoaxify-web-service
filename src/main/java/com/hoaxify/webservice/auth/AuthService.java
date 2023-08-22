package com.hoaxify.webservice.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hoaxify.webservice.error.AuthorizationException;
import com.hoaxify.webservice.error.NotFoundException;
import com.hoaxify.webservice.user.UserService;
import com.hoaxify.webservice.user.Users;
import com.hoaxify.webservice.user.vm.UserVM;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    UserService userService;
    PasswordEncoder passwordEncoder;

    private final String SECRET_KEY = "secret-key";

    private final int ONE_DAY = 24 * 60 * 60 * 1000;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(Credentials credentials) {
        Users inDb = userService.getUserByUserName(credentials.getUserName());
        if(inDb == null) {
            throw new AuthorizationException();
        }
        boolean isMatched = passwordEncoder.matches(credentials.getPass(), inDb.getPass());
        if(!isMatched) {
            throw new AuthorizationException();
        }
        UserVM userVM = new UserVM(inDb);
        String token;
        try {
            Algorithm algorithm = Algorithm.HMAC512(SECRET_KEY);
            token = JWT.create()
                    .withIssuer("hoaxify")
                    .withSubject("" + inDb.getId())
                    .withExpiresAt(new java.util.Date(System.currentTimeMillis() + ONE_DAY))
                    .sign(algorithm);

        } catch (JWTCreationException exception){
            throw new AuthorizationException();
        }
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setUser(userVM);
        return authResponse;
    }

    @Transactional
    public UserDetails getUserDetails(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC512(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("hoaxify")
                    .build();
            decodedJWT = verifier.verify(token);
        } catch (Exception e) {
            throw new AuthorizationException();
        }
        String userId = decodedJWT.getSubject();

        return userService.getUserById(Long.parseLong(userId));
    }
}
