package com.hoaxify.webservice.user;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    @Autowired//Eğer tek bir tane set edilecek obje varsa autowired'a gerek yok
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users getUserById(long id){
       return userRepository.getUserById(id);
    }

    public Users getUserByUserName(String userName){
        return userRepository.getUserByUserName(userName);
    }

    public void saveUser(Users user){
        user.setPass(this.passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Page<Users> getUsers(Pageable pageConf){
        return userRepository.findAll(pageConf);
    }
}
