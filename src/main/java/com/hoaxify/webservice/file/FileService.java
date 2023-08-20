package com.hoaxify.webservice.file;

import com.hoaxify.webservice.user.Users;
import jakarta.transaction.Transactional;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@EnableScheduling
public class FileService {

    private static final int TEN_SECONDS = 10 * 1000;
    private static final int ONE_HOUR = 60 * 60 * 1000;
    private static final int ONE_DAY = 24 * ONE_HOUR;

    @Value("${file-upload-dir}")
    String uploadDir;

    @Value("${file-upload-hoax-attachements-dir}")
    String hoaxFileUploadDir;

    FileAttachmentRepository fileAttachmentRepository;

    public FileService(FileAttachmentRepository fileAttachmentRepository) {
        this.fileAttachmentRepository = fileAttachmentRepository;
    }

    public String writeBase64EncodedStringToFile(String image) throws IOException {
        String fileName = generateRandomName();
        File target = new File(uploadDir + "/" + fileName);
        OutputStream io = new FileOutputStream(target);
        byte[] base64encoded = Base64.getDecoder().decode(image);
        io.write(base64encoded);
        io.close();
        return fileName;
    }

    public boolean deleteFile(String fileName, int deleteType) throws IOException {
        if (deleteType == 1) {
            String targetPath = uploadDir + "/" + hoaxFileUploadDir + "/" + fileName;
            return Files.deleteIfExists(Paths.get(targetPath));
        }
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

    public FileAttachment saveHoaxAttachment(MultipartFile file) {
        String fileName = generateRandomName();
        File target = new File(uploadDir + "/" + hoaxFileUploadDir + "/" + fileName);
        try {
            OutputStream outputStream = new FileOutputStream(target);
            outputStream.write(file.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        FileAttachment fileAttachment = new FileAttachment();
        fileAttachment.setName(fileName);
        return fileAttachmentRepository.save(fileAttachment);
    }

    @Transactional
    public boolean cancelFileAttachment(FileAttachment file) {
        boolean result = false;
        try {
           result = this.deleteFile(file.getName(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        String id = String.valueOf(file.getId());
        fileAttachmentRepository.deleteByHoaxIdIsNullAndId(Long.parseLong(id));
        return result;
    }

    /**
     * ||Scheduling Method||
     *
     * This method is used to delete files that are not attached to any hoax and older than 1 hours
     */
    @Scheduled(fixedRate = ONE_HOUR)
    public void cleanupStorage() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<FileAttachment> willBeDeletedFiles = fileAttachmentRepository.findByDateBeforeAndHoaxIsNull(oneHourAgo);
        for (FileAttachment file : willBeDeletedFiles) {
            try {
                this.deleteFile(file.getName(), 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fileAttachmentRepository.deleteById(file.getId());
        }
    }


    public void deleteAllStoredFilesForUser(Users inDB) {
        List<FileAttachment> files = fileAttachmentRepository.findByHoaxUser(inDB);
        try {
            deleteFile(inDB.getImage(), 0);

            for(FileAttachment file : files){
                deleteFile(file.getName(), 1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
