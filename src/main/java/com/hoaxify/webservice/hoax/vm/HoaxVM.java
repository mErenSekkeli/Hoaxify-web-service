package com.hoaxify.webservice.hoax.vm;

import com.hoaxify.webservice.file.vm.FileAttachmentVM;
import com.hoaxify.webservice.hoax.Hoaxes;
import com.hoaxify.webservice.user.vm.UserVM;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HoaxVM {
    private long id;
    private String content;
    private LocalDateTime timestamp;
    private UserVM user;
    private FileAttachmentVM fileAttachment;
    private long likeCount;
    private long commentCount;

    public HoaxVM(Hoaxes hoax) {
        this.id = hoax.getId();
        this.content = hoax.getContent();
        this.timestamp = hoax.getTimestamp();
        this.user = new UserVM(hoax.getUser());
        this.fileAttachment = (hoax.getFileAttachment() != null) ? new FileAttachmentVM(hoax.getFileAttachment()) : null;
        this.likeCount = hoax.getLikeCount();
        this.commentCount = hoax.getCommentCount();
    }
}
