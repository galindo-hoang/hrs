package org.example.hrs.repository;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadImageRepository {
    List<String> save(String bucketName, List<MultipartFile> files);
    List<String> getAllImages(String path);
    Boolean delete(String path);
}
