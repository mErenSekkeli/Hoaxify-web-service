package com.hoaxify.webservice.hoax;

import com.hoaxify.webservice.comment.Comments;
import com.hoaxify.webservice.file.FileAttachment;
import com.hoaxify.webservice.like.Likes;
import com.hoaxify.webservice.user.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(indexes = {@Index(columnList = "user_id", name = "hoax_id_index")})
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

    @OneToOne(mappedBy = "hoax", cascade = CascadeType.REMOVE)
    private FileAttachment fileAttachment;

    @OneToMany(mappedBy = "hoax", cascade = CascadeType.REMOVE)
    private List<Likes> likes;

    @OneToMany(mappedBy = "hoax", cascade = CascadeType.REMOVE)
    private List<Comments> comments;

    @Column(columnDefinition = "integer default 0")
    private long likeCount;

    @Column(columnDefinition = "integer default 0")
    private long commentCount;


}
