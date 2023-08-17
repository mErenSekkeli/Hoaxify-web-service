package com.hoaxify.webservice.file;

import com.hoaxify.webservice.hoax.Hoaxes;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(indexes = {@Index(columnList = "hoax_id", name = "file_attachment_hoax_id_index")})
public class FileAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @CreationTimestamp
    private LocalDateTime date;

    @OneToOne
    private Hoaxes hoax;

}
