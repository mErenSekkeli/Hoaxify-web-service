package com.hoaxify.webservice.user;

import com.hoaxify.webservice.error.NotFoundException;
import com.hoaxify.webservice.user.vm.UserUpdateVM;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        Users user = userRepository.findByUserName(userName);
        if(user == null){
            throw new NotFoundException();
        }
        return user;
    }

    public void saveUser(Users user){
        user.setPass(this.passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Page<Users> getUsers(Pageable pageConf, Users user){
        Sort sort = Sort.by(Sort.Direction.ASC, "userName");
        pageConf = org.springframework.data.domain.PageRequest.of(pageConf.getPageNumber(), pageConf.getPageSize(), sort);
        if(user != null) {
            return userRepository.findByUserNameNot(user.getUserName(), pageConf);
        }
        return userRepository.findAll(pageConf);
    }

    public Users updateUser(String username, UserUpdateVM userUpdateVM) {
        Users inDB = getUserByUserName(username);
        inDB.setName(userUpdateVM.getName());
        inDB.setSurname(userUpdateVM.getSurname());
        return userRepository.save(inDB);
    }
}
