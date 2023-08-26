package com.hoaxify.webservice.like;

import com.hoaxify.webservice.hoax.Hoaxes;
import com.hoaxify.webservice.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, String> {

    Likes findByUserAndHoax(Users user, Hoaxes hoax);
    List<Likes> findByUser(Users user);
}
