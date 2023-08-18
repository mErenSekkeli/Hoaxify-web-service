package com.hoaxify.webservice.hoax.vm;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HoaxSubmitVM {

    @Size(min = 1, max = 255)
    private String content;

    private long attachmentId;
}
