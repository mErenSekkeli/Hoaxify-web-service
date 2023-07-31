package com.hoaxify.webservice.file;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileService {

    @Value("${file-upload-dir}")
    String uploadDir;

    public String writeBase64EncodedStringToFile(String image) throws IOException {
        String fileName = generateRandomName();
        File target = new File(uploadDir + "/" + fileName);
        OutputStream io = new FileOutputStream(target);
        byte[] base64encoded = Base64.getDecoder().decode(image);
        io.write(base64encoded);
        io.close();
        return fileName;
    }

    public boolean deleteFile(String fileName) throws IOException {
        String targetPath = uploadDir + "/" + fileName;
        return Files.deleteIfExists(Paths.get(targetPath));
    }

    private String generateRandomName() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String evaluateFileType(String file) {
        Tika tika = new Tika();
        return tika.detect(Base64.getDecoder().decode(file));
    }
}
