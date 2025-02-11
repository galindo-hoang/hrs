package org.example.hrs.repository.impl;

import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.example.hrs.exception.base.TechnicalException;
import org.example.hrs.repository.UploadImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UploadImageRepositoryMinio implements UploadImageRepository {
    private final MinioClient minioClient;

    @Value("${minio.url}")
    private String url;

    @Override
    public List<String> save(String bucketName, List<MultipartFile> files) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            List<String> res = new ArrayList<>();
            for (MultipartFile file : files) {
                try (InputStream inputStream = file.getInputStream()) {
                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(file.getOriginalFilename())
                                    .stream(inputStream, file.getSize(), -1)
                                    .contentType(file.getContentType())
                                    .build()
                    );
                    res.add(url + "/" + bucketName + "/" + file.getOriginalFilename());
                } catch (Exception e) {
                    System.out.println(file.getOriginalFilename() + e.getMessage());
//                    throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
                }
            }
            return res;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new TechnicalException(HttpStatus.BAD_REQUEST, e);
        }
    }

    @Override
    public List<String> getAllImages(String bucketName) {
        List<String> files = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).build()
        );

        for (Result<Item> result : results) {
            try {
                files.add(url + "/" + bucketName + "/" + result.get().objectName());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return files;
    }

    @Override
    public Boolean delete(String path) {
        try {
            // Delete all files before deleting the bucket
            Iterable<Result<Item>> objects = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(path).build()
            );

            for (Result<Item> object : objects) {
                String objectName = object.get().objectName();
                minioClient.removeObject(
                        RemoveObjectArgs.builder().bucket(path).object(objectName).build()
                );
            }

            // Delete the bucket
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(path).build());
            return true;
        } catch (Exception e) {
            throw new TechnicalException(HttpStatus.BAD_REQUEST, e);
        }
    }
}
