package com.hoaxify.webservice.user.vm;

import com.hoaxify.webservice.shared.FileType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateVM {
    @NotNull
    @Size(min = 4, max = 255)
    private String name;
    @NotNull
    @Size(min = 4, max = 255)
    private String surname;
    private String userName;
    @FileType(types = {"image/png", "image/jpeg"})
    private String image;
}
