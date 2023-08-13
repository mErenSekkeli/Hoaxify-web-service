package com.hoaxify.webservice.hoax;

import com.hoaxify.webservice.user.UserService;
import com.hoaxify.webservice.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Page<Hoaxes> getOldHoaxes(long id, String username, Pageable page) {
        Specification<Hoaxes> specification = Specification.where(idLessThan(id));
        if(username != null){
            Users inDB = userService.getUserByUserName(username);
            specification = specification.and(userIs(inDB));
            return hoaxRepository.findAll(specification, page);
        }
        return hoaxRepository.findAll(specification, page);
    }

    public Page<Hoaxes> getHoaxesOfUser(String username, Pageable page){
        Users inDB = userService.getUserByUserName(username);
        return hoaxRepository.findByUser(inDB, page);
    }

    public long getNewHoaxesCount(long id, String username) {
        Specification<Hoaxes> specification = Specification.where(idGreaterThan(id));
        if(username != null){
            Users inDB = userService.getUserByUserName(username);
            specification = specification.and(userIs(inDB));
        }
        return hoaxRepository.count(specification);
    }

    public List<Hoaxes> getNewHoaxes(long id, String username, Sort sort) {
        if(username != null){
            Users inDB = userService.getUserByUserName(username);
            return hoaxRepository.findTop10ByIdGreaterThanAndUser(id, inDB, sort);
        }
        return hoaxRepository.findTop10ByIdGreaterThan(id, sort);
    }

    private Specification<Hoaxes> idLessThan(long id){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("id"), id);
    }

    private Specification<Hoaxes> userIs(Users user){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
    }

    private Specification<Hoaxes> idGreaterThan(long id){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("id"), id);
    }

}
