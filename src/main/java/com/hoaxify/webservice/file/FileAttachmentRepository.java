package com.hoaxify.webservice.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {

    void deleteByHoaxIdIsNullAndId(long id);

    List<FileAttachment> findByDateBeforeAndHoaxIsNull(LocalDateTime date);
}
