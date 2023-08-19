package com.hoaxify.webservice.hoax;

import com.hoaxify.webservice.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service(value = "hoaxSecurity")
public class HoaxSecurityService {

    @Autowired
    HoaxRepository hoaxRepository;

    public boolean isAllowedToDelete(long id, Users loggedInUser) {
        Optional<Hoaxes> inDb = hoaxRepository.findById(id);
        return inDb.filter(hoaxes -> hoaxes.getUser().getId() == loggedInUser.getId()).isPresent();
    }

}
