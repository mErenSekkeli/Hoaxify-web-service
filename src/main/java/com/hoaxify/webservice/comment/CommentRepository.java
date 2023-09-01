package com.hoaxify.webservice.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentRepository extends JpaRepository<Comments, Long>, JpaSpecificationExecutor<Comments> {

    Page<Comments> findByHoaxId(long id, Pageable page);


}
