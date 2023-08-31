package com.hoaxify.webservice.comment;

import com.hoaxify.webservice.comment.vm.CommentSubmitVM;
import com.hoaxify.webservice.error.AuthorizationException;
import com.hoaxify.webservice.error.NotFoundException;
import com.hoaxify.webservice.hoax.HoaxRepository;
import com.hoaxify.webservice.hoax.Hoaxes;
import com.hoaxify.webservice.user.UserRepository;
import com.hoaxify.webservice.user.Users;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    CommentRepository commentRepository;
    HoaxRepository hoaxRepository;
    UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, HoaxRepository hoaxRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.hoaxRepository = hoaxRepository;
        this.userRepository = userRepository;
    }

    public void save(CommentSubmitVM comment, long id) {
        Optional<Hoaxes> inDbHoax = hoaxRepository.findById(id);
        if(inDbHoax.isEmpty()){
            throw new NotFoundException();
        }
        Users inDbUser = userRepository.findByUserName(comment.getUsername());
        if(inDbUser == null){
            throw new NotFoundException();
        }
        Comments comments = new Comments();
        comments.setContent(comment.getContent());
        comments.setHoax(inDbHoax.get());
        comments.setUser(inDbUser);
        commentRepository.save(comments);
    }
}
