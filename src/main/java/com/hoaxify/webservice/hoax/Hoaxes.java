package com.hoaxify.webservice.hoax;

import com.hoaxify.webservice.user.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(indexes = {@Index(columnList = "id", name = "hoax_id_index")})
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

    @ManyToOne
    private Users user;


}
