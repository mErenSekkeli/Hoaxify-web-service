package com.hoaxify.webservice.comment;

import com.hoaxify.webservice.comment.vm.CommentSubmitVM;
import com.hoaxify.webservice.comment.vm.CommentVM;
import com.hoaxify.webservice.error.AuthorizationException;
import com.hoaxify.webservice.error.NotFoundException;
import com.hoaxify.webservice.hoax.HoaxRepository;
import com.hoaxify.webservice.hoax.Hoaxes;
import com.hoaxify.webservice.user.UserRepository;
import com.hoaxify.webservice.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public Page<Comments> getCommentsOfHoax(long id, Pageable page) {
        Page<Comments> comments = commentRepository.findByHoaxId(id, page);

        List<CommentVM> commentVMs = new ArrayList<>();
        for(Comments comment : comments){
            CommentVM commentVM = new CommentVM(comment);
            commentVM.setId(comment.getId());
            commentVM.setContent(comment.getContent());
            commentVM.setTimestamp(comment.getDate());
            commentVMs.add(commentVM);
        }
        return comments;
    }

    public void deleteComment(long id) {
        Optional<Comments> inDbComment = commentRepository.findById(id);
        if(inDbComment.isEmpty()){
            throw new NotFoundException();
        }
        commentRepository.deleteById(id);
    }

    public Page<Comments> getOldComments(Long id, Long commentId, Pageable page) {
        Specification<Comments> specification = Specification.where(idLessThan(commentId));
        specification = specification.and(hoaxIdEquals(id));
        return commentRepository.findAll(specification, page);
    }

    private Specification<Comments> idLessThan(long id){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("id"), id);
    }

    private Specification<Comments> hoaxIdEquals(long hoaxId){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("hoax").get("id"), hoaxId);
    }
}
