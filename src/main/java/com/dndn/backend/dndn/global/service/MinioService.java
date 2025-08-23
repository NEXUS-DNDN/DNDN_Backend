package com.dndn.backend.dndn.global.service;

import com.dndn.backend.dndn.domain.user.exception.DocumentException;
import com.dndn.backend.dndn.global.error.code.status.ErrorStatus;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    // 파일 업로드
    public String uploadFile(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String storedName = UUID.randomUUID() + "_" + originalName;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(storedName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return storedName;
        } catch (Exception e) {
            throw new DocumentException(ErrorStatus.DOCUMENT_UPLOAD_FAILED);
        }
    }

    // 파일 다운로드
    public InputStream downloadFile(String storedName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(storedName)
                            .build()
            );
        } catch (Exception e) {
            throw new DocumentException(ErrorStatus.DOCUMENT_DOWNLOAD_FAILED);
        }
    }

    // Presigned URL 생성
    public String getPresignedUrl(String storedName, int expirySeconds) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(storedName)
                            .expiry(expirySeconds)
                            .build()
            );
        } catch (Exception e) {
            throw new DocumentException(ErrorStatus.DOCUMENT_DOWNLOAD_FAILED);
        }
    }

    // 파일 삭제
    public void deleteFile(String storedName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(storedName)
                            .build()
            );
        } catch (Exception e) {
            throw new DocumentException(ErrorStatus.DOCUMENT_DELETE_FAILED);
        }
    }
}