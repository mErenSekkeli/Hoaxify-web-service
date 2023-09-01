package com.hoaxify.webservice.comment;

import com.hoaxify.webservice.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service(value = "CommentSecurity")

public class CommentSecurityService {

    @Autowired
    CommentRepository commentRepository;

    public boolean isAllowedToDelete(long id, Users loggedInUser) {
        Optional<Comments> inDb = commentRepository.findById(id);
        return inDb.filter(comments -> comments.getUser().getId() == loggedInUser.getId()).isPresent();
    }
}
