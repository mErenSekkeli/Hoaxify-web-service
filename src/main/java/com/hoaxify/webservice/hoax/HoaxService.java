package com.hoaxify.webservice.hoax;

import com.hoaxify.webservice.user.UserService;
import com.hoaxify.webservice.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HoaxService {

    @Autowired
    HoaxRepository hoaxRepository;

    @Autowired
    UserService userService;


    public void saveHoax(Hoaxes hoax, Users user) {
        hoax.setUser(user);
        hoaxRepository.save(hoax);
    }

    public Page<Hoaxes> getHoaxList(Pageable page){
        return hoaxRepository.findAll(page);
    }

    public Page<Hoaxes> getOldHoaxes(long id, Pageable page) {
        return hoaxRepository.findByIdLessThan(id, page);
    }

    public Page<Hoaxes> getHoaxesOfUser(String username, Pageable page){
        Users inDB = userService.getUserByUserName(username);
        return hoaxRepository.findByUser(inDB, page);
    }

    public Page<Hoaxes> getOldHoaxesOfUser(long id, String username, Pageable page){
        Users inDB = userService.getUserByUserName(username);
        return hoaxRepository.findByIdLessThanAndUser(id, inDB, page);
    }

    public long getNewHoaxesCount(long id, String username) {
        if(username != null){
            Users inDB = userService.getUserByUserName(username);
            return hoaxRepository.countByIdGreaterThanAndUser(id, inDB);
        }
        return hoaxRepository.countByIdGreaterThan(id);
    }

}
