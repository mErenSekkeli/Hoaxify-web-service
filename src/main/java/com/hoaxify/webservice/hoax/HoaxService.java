package com.hoaxify.webservice.hoax;

import com.hoaxify.webservice.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HoaxService {

    @Autowired
    HoaxRepository hoaxRepository;


    public void saveHoax(Hoaxes hoax) {
        hoaxRepository.save(hoax);
    }

    public Page<Hoaxes> getHoaxList(Pageable page, Users loggedInUser){
        //loggedInUser will be used when user hoax relation is created
        return hoaxRepository.findAll(page);
    }
}
