package com.hoaxify.webservice.user;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hoaxify.webservice.auth.Token;
import com.hoaxify.webservice.hoax.Hoaxes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity
@Table(indexes = {@Index(columnList = "id", name = "user_id_index")})
@Getter
public class Users implements UserDetails {
    @Id
    @GeneratedValue
    private long id;
    @NotNull(message = "{hoaxify.constraint.name.NotNull.message}")
    @Size(min = 4, max = 250)
    private String name;
    @NotNull(message = "{hoaxify.constraint.surname.NotNull.message}")
    @Size(min = 4, max = 250)
    private String surname;
    @NotNull(message = "{hoaxify.constraint.userName.NotNull.message}")
    @Size(min = 4, max = 250)
    @UniqueUsername(message = "{hoaxify.constraint.userName.uniqueUserName.message}")
    private String userName;
    @NotNull(message = "{hoaxify.constraint.pass.NotNull.message}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*])(?=\\S+$).{8,}$",
             message = "{hoaxify.constraint.pass.pattern.message}")
    private String pass;
    private String image;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Hoaxes> hoaxes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Token> tokens;


    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
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
