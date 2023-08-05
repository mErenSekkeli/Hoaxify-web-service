package com.hoaxify.webservice.hoax;

import com.hoaxify.webservice.hoax.vm.HoaxUpdateVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HoaxService {

    @Autowired
    HoaxRepository hoaxRepository;


    public void saveHoax(Hoaxes hoax) {
        hoaxRepository.save(hoax);
    }
}
