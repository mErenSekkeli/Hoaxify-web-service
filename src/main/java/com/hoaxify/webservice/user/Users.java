package com.hoaxify.webservice.user;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.hoaxify.webservice.shared.Views;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Entity
@Table
public class Users implements UserDetails {
    @Id
    @GeneratedValue
    private long id;
    @NotNull(message = "{hoaxify.constraint.name.NotNull.message}")
    @Size(min = 5, max = 250)
    @JsonView(Views.Public.class)
    private String name;
    @NotNull(message = "{hoaxify.constraint.surname.NotNull.message}")
    @Size(min = 5, max = 250)
    @JsonView(Views.Public.class)
    private String surname;
    @NotNull(message = "{hoaxify.constraint.userName.NotNull.message}")
    @Size(min = 5, max = 250)
    @UniqueUsername(message = "{hoaxify.constraint.userName.uniqueUserName.message}")
    private String userName;
    @NotNull(message = "{hoaxify.constraint.pass.NotNull.message}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
             message = "{hoaxify.constraint.pass.pattern.message}")
    private String pass;
    @JsonView(Views.Public.class)
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("Role_user");
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
