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
    public static DocumentResponseDTO.DocumentDownloadResponse toDocumentDownloadResponse(
            DocumentFile document, String presignedUrl) {
        return DocumentResponseDTO.DocumentDownloadResponse.builder()
                .originalName(document.getOriginalName())
                .downloadUrl(presignedUrl)
                .build();
    }

}
