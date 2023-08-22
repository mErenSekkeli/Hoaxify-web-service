package com.hoaxify.webservice.auth;

import com.hoaxify.webservice.user.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Token {
    @Id
    private String token;
    @ManyToOne
    private Users user;
    @CreationTimestamp
    private LocalDateTime date;
}

