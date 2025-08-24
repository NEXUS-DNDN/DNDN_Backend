package com.dndn.backend.dndn.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class DocumentResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DocumentUploadResponse {

        private String originalName;
        private String storedName;
        private String url;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentListItemResponse {
        private Long documentId;
        private String originalName;
        private String storedName;
        private String url;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DocumentDownloadResponse {
        private String originalName;
        private String downloadUrl;
    }
}
