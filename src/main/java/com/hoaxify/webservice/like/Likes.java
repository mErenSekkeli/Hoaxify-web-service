package com.hoaxify.webservice.like;

import com.hoaxify.webservice.hoax.Hoaxes;
import com.hoaxify.webservice.user.Users;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(indexes = {@Index(columnList = "user_id, hoax_id", name = "user_hoax_index")})
@Data
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Hoaxes hoax;

    @CreationTimestamp
    private LocalDateTime date;

}