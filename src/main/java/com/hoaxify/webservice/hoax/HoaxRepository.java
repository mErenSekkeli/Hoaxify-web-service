package com.hoaxify.webservice.hoax;

import com.hoaxify.webservice.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoaxRepository extends JpaRepository<Hoaxes, Long> {

    Page<Hoaxes> findByUser(Users user, Pageable page);

}
