package com.hoaxify.webservice.user;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Entity
@Table
public class Users {
    @Id
    @GeneratedValue
    private long id;
    @NotNull(message = "{hoaxify.constraint.name.NotNull.message}")
    @Size(min = 5, max = 250)
    private String name;
    @NotNull(message = "{hoaxify.constraint.surname.NotNull.message}")
    @Size(min = 5, max = 250)
    private String surname;
    @NotNull(message = "{hoaxify.constraint.userName.NotNull.message}")
    @Size(min = 5, max = 250)
    @UniqueUsername(message = "{hoaxify.constraint.userName.uniqueUserName.message}")
    private String userName;
    @NotNull(message = "{hoaxify.constraint.pass.NotNull.message}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
             message = "{hoaxify.constraint.pass.pattern.message}")
    private String pass;

    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", userName='" + userName + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
