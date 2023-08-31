package com.hoaxify.webservice.comment.vm;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentSubmitVM {

    private long hoaxId;
    private String username;
    @Size(min = 1, max = 255)
    private String content;
}
