package com.hoaxify.webservice.user;

import com.hoaxify.webservice.error.NotFoundException;
import com.hoaxify.webservice.file.FileService;
import com.hoaxify.webservice.hoax.HoaxService;
import com.hoaxify.webservice.user.vm.UserUpdateVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.*;


@Service
public class UserService {

    UserRepository userRepository;
    FileService fileService;
    PasswordEncoder passwordEncoder;

    @Autowired//Eğer tek bir tane set edilecek obje varsa autowired'a gerek yok
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FileService fileService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileService = fileService;
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
        if(userUpdateVM.getImage() != null){
            String oldImageName = inDB.getImage();

            try {
                String fileName = fileService.writeBase64EncodedStringToFile(userUpdateVM.getImage());
                inDB.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(oldImageName != null) {
                try {
                    fileService.deleteFile(oldImageName, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return userRepository.save(inDB);
    }

    public void deleteUser(String username) {
        //hoaxService.deleteAllHoaxesOfUser(username); // This part is not necessary because of the cascade type
        Users inDB = getUserByUserName(username);
        fileService.deleteAllStoredFilesForUser(inDB);
        userRepository.delete(inDB);
    }
}
