package com.hoaxify.webservice.auth;

import com.hoaxify.webservice.error.AuthorizationException;
import com.hoaxify.webservice.user.UserService;
import com.hoaxify.webservice.user.Users;
import com.hoaxify.webservice.user.vm.UserVM;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableScheduling
public class AuthService {

    UserService userService;
    PasswordEncoder passwordEncoder;
    TokenRepository tokenRepository;

    private final long ONE_MONTH = (long) 30 * 24 * 60 * 60 * 1000;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }
    public AuthResponse authenticate(Credentials credentials) {
        Users inDb = userService.getUserByUserName(credentials.getUserName());
        if(inDb == null) {
            throw new AuthorizationException("Not Authorized");
        }
        boolean isMatched = passwordEncoder.matches(credentials.getPass(), inDb.getPass());
        if(!isMatched) {
            throw new AuthorizationException("Not Authorized");
        }
        UserVM userVM = new UserVM(inDb);

        String token = generateRandomToken();
        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setUser(inDb);
        tokenRepository.save(tokenEntity);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setUser(userVM);
        return authResponse;
    }

    public String generateRandomToken() {
        String random = UUID.randomUUID().toString().replaceAll("-", "");
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[128];
        secureRandom.nextBytes(key);
        return random + Base64.getUrlEncoder().withoutPadding().encodeToString(key).replaceAll("-", "");
    }

    @Transactional
    public UserDetails getUserDetails(String token) {
        Optional<Token> inDbToken = tokenRepository.findById(token);
        return inDbToken.<UserDetails>map(Token::getUser).orElse(null);
    }

    @Scheduled(fixedRate = ONE_MONTH)
    public void cleanExpiredTokens() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Token> expiredTokens = tokenRepository.findByDateBefore(oneMonthAgo);
        tokenRepository.deleteAll(expiredTokens);
    }

    public void clearToken(String token) {
        Optional<Token> inDbToken = tokenRepository.findById(token);
        inDbToken.ifPresent(value -> tokenRepository.delete(value));
    }
}
