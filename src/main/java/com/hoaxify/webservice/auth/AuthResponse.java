package com.hoaxify.webservice.auth;

import com.hoaxify.webservice.user.vm.UserVM;
import lombok.Data;

@Data
public class AuthResponse {

    private String token;

    private UserVM user;
}
