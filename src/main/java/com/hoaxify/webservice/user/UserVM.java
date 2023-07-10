package com.hoaxify.webservice.user;

import lombok.Data;

@Data
public class UserVM {

    private String name;
    private String surname;
    private String userName;
    private String image;

    public UserVM(Users user) {
        this.setName(user.getName());
        this.setSurname(user.getSurname());
        this.setUserName(user.getUserName());
        this.setImage(user.getImage());
    }

}
