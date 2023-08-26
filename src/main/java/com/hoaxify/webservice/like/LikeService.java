package com.hoaxify.webservice.like;

import com.hoaxify.webservice.error.NotFoundException;
import com.hoaxify.webservice.hoax.HoaxRepository;
import com.hoaxify.webservice.hoax.Hoaxes;
import com.hoaxify.webservice.like.vm.LikeSubmitVM;
import com.hoaxify.webservice.like.vm.LikeVM;
import com.hoaxify.webservice.user.UserRepository;
import com.hoaxify.webservice.user.Users;
import com.hoaxify.webservice.user.vm.UserVM;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    LikeRepository likeRepository;
    HoaxRepository hoaxRepository;

    UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, HoaxRepository hoaxRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.hoaxRepository = hoaxRepository;
        this.userRepository = userRepository;
    }

    public void like(LikeSubmitVM like) {
        Optional<Hoaxes> optionalHoax = hoaxRepository.findById(like.getHoaxId());
        Users inDbUser = userRepository.findByUserName(like.getUsername());
        if(optionalHoax.isEmpty()){
            throw new NotFoundException();
        }
        Hoaxes hoax = optionalHoax.get();
        Likes likes = new Likes();
        likes.setHoax(hoax);
        likes.setUser(inDbUser);
        likeRepository.save(likes);
    }

    public void unlike(long id, String username) {
        Optional<Hoaxes> optionalHoax = hoaxRepository.findById(id);
        Users inDbUser = userRepository.findByUserName(username);
        if(optionalHoax.isEmpty()){
            throw new NotFoundException();
        }
        Hoaxes hoax = optionalHoax.get();
        Likes likes = likeRepository.findByUserAndHoax(inDbUser, hoax);
        likeRepository.delete(likes);
    }

    public LikeVM getUsersLikes(String username) {
        Users inDbUser = userRepository.findByUserName(username);
        List<Likes> likes = likeRepository.findByUser(inDbUser);
        LikeVM likeVM = new LikeVM();
        likeVM.setUsername(username);
        likeVM.setHoaxIds(likes.stream().map(Likes::getHoax).map(Hoaxes::getId).toList());
        return likeVM;
    }
}
