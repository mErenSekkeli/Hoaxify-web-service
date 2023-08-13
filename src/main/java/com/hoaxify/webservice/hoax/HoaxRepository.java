package com.hoaxify.webservice.hoax;

import com.hoaxify.webservice.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HoaxRepository extends JpaRepository<Hoaxes, Long>, JpaSpecificationExecutor<Hoaxes> {

    Page<Hoaxes> findByUser(Users user, Pageable page);

//    Page<Hoaxes> findByIdLessThan(long id, Pageable page);

//    Page<Hoaxes> findByIdLessThanAndUser(long id, Users user, Pageable page);

//    long countByIdGreaterThan(long id);

//    long countByIdGreaterThanAndUser(long id, Users user);

    List<Hoaxes> findTop10ByIdGreaterThan(long id, Sort sort);

    List<Hoaxes> findTop10ByIdGreaterThanAndUser(long id, Users user, Sort sort);

}
