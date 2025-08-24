package com.dndn.backend.dndn.domain.user.application;

import com.dndn.backend.dndn.domain.user.dto.DocumentResponseDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    // 서류 업로드
    DocumentResponseDTO.DocumentUploadResponse uploadFile(MultipartFile file, Long userId);

    // 서류 조회
    List<DocumentResponseDTO.DocumentListItemResponse> getUserDocuments(Long userId);

    // 서류 다운로드
    DocumentResponseDTO.DocumentDownloadResponse downloadDocument(Long userId, Long documentId);

    // 문서 삭제
    public void deleteDocument(Long userId, Long documentId);

}