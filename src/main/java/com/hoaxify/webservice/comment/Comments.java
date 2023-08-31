package com.hoaxify.webservice.comment;

import com.hoaxify.webservice.hoax.Hoaxes;
import com.hoaxify.webservice.user.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(indexes = {@Index(columnList = "user_id, hoax_id", name = "user_hoax_index_comment")})
@Data
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Hoaxes hoax;

    @Size(min = 1, max = 255)
    private String content;

    @CreationTimestamp
    private LocalDateTime date;

}
