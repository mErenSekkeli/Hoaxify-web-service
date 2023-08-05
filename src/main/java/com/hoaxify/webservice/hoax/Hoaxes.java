package com.hoaxify.webservice.hoax;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class Hoaxes {

    @Id
    @GeneratedValue
    private long id;
    @Size(min = 1, max = 255)
    private String content;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;


}
