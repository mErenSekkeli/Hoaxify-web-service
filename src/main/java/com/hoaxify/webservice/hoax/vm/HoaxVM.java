package com.hoaxify.webservice.hoax.vm;

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

    public HoaxVM(Hoaxes hoax) {
        this.id = hoax.getId();
        this.content = hoax.getContent();
        this.timestamp = hoax.getTimestamp();
        this.user = new UserVM(hoax.getUser());
    }
}
