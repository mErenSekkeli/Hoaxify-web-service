package com.hoaxify.webservice.comment.vm;

import com.hoaxify.webservice.comment.Comments;
import com.hoaxify.webservice.user.vm.UserVM;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentVM {

    private long id;
    private String content;
    private LocalDateTime timestamp;
    private long hoaxId;
    private UserVM user;

    public CommentVM(Comments comments) {
        this.setId(comments.getId());
        this.setContent(comments.getContent());
        this.setTimestamp(comments.getDate());
        this.setHoaxId(comments.getHoax().getId());
        this.setUser(new UserVM(comments.getUser()));
    }

}
