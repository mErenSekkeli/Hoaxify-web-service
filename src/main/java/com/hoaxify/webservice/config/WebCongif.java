package com.hoaxify.webservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebCongif implements WebMvcConfigurer {

    @Value("${file-upload-dir}")
    String uploadDir;

    @Value("${file-upload-hoax-attachements-dir}")
    String hoaxFileUploadDir;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("file:./" + uploadDir + "/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));
    }

    @Bean
    CommandLineRunner createStorageDirectoriesIfNotExists() {
        return (args) -> {
            File folder = new File(uploadDir);
            boolean folderExist = folder.exists() && folder.isDirectory();
            boolean hoaxFolderExist = new File(uploadDir + "/" + hoaxFileUploadDir).exists() && new File(uploadDir + "/" + hoaxFileUploadDir).isDirectory();
            if (!folderExist) {
                folder.mkdir();
            }
            if (!hoaxFolderExist) {
                new File(uploadDir + "/" + hoaxFileUploadDir).mkdir();
            }
        };
    }
}
