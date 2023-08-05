package com.hoaxify.webservice.hoax.vm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HoaxUpdateVM {

    @NotNull
    @Size(min = 1, max = 255)
    private String content;
}
