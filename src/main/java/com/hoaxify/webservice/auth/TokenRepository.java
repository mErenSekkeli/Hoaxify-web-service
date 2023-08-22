package com.hoaxify.webservice.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenRepository extends JpaRepository<Token, String> {

    List<Token> findByDateBefore(LocalDateTime date);

}
