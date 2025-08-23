package com.dndn.backend.dndn.domain.user.converter;

import com.dndn.backend.dndn.domain.user.domain.entity.DocumentFile;
import com.dndn.backend.dndn.domain.user.dto.DocumentResponseDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UserConverter {

    // 업로드 응답
    public static DocumentResponseDTO.DocumentUploadResponse toDocumentUploadResponse(
            String originalName,
            String storedName,
            String url
    ) {
        return DocumentResponseDTO.DocumentUploadResponse.builder()
                .originalName(originalName)
                .storedName(storedName)
                .url(url)
                .build();
    }

    // 목록 조회 응답
    public static DocumentResponseDTO.DocumentListItemResponse toDocumentListItemResponse(
            DocumentFile document) {
        return DocumentResponseDTO.DocumentListItemResponse.builder()
                .documentId(document.getId())
                .originalName(document.getOriginalName())
                .storedName(document.getStoredName())
                .url(document.getUrl())
                .createdAt(document.getCreatedAt())
                .build();
    }

    // 다운로드 응답
    public static ResponseEntity<InputStreamResource> toDocumentDownloadResponse(
            DocumentFile document, InputStream inputStream) {

        InputStreamResource resource = new InputStreamResource(inputStream);

        String downloadName = document.getOriginalName() != null
                ? document.getOriginalName()
                : document.getStoredName();

        // 👉 파일명 한글 깨짐/에러 방지
        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(downloadName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20"); // 공백 처리
        } catch (Exception e) {
            encodedFileName = "download-file";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }



}
