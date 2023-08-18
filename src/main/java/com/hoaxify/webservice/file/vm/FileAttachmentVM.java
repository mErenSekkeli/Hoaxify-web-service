package com.hoaxify.webservice.file.vm;

import com.hoaxify.webservice.file.FileAttachment;
import lombok.Data;

@Data
public class FileAttachmentVM {

    private String name;

    public FileAttachmentVM(FileAttachment fileAttachment) {
        this.name = fileAttachment.getName();
    }
}
