package com.hoaxify.webservice.file;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {

    void deleteByHoaxIdIsNullAndId(long id);
}
