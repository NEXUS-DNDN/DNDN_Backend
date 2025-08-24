package com.dndn.backend.dndn.domain.user.application;

import com.dndn.backend.dndn.domain.user.converter.UserConverter;
import com.dndn.backend.dndn.domain.user.domain.entity.DocumentFile;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.user.domain.repository.DocumentRepository;
import com.dndn.backend.dndn.domain.user.domain.repository.UserRepository;
import com.dndn.backend.dndn.domain.user.dto.DocumentResponseDTO;
import com.dndn.backend.dndn.domain.user.exception.DocumentException;
import com.dndn.backend.dndn.domain.user.exception.UserException;
import com.dndn.backend.dndn.global.error.code.status.ErrorStatus;
import com.dndn.backend.dndn.global.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final MinioService minioService;

    // 서류 업로드
    @Transactional
    @Override
    public DocumentResponseDTO.DocumentUploadResponse uploadFile(MultipartFile file, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus._USER_NOT_FOUND));

        String originalName = file.getOriginalFilename();
        String storedName = minioService.uploadFile(file); // 실패 시 DocumentException 던짐
        String url = "/minio/" + storedName;

        DocumentFile document = DocumentFile.builder()
                .user(user)
                .originalName(originalName)
                .storedName(storedName)
                .url(url)
                .build();

        documentRepository.save(document);

        return UserConverter.toDocumentUploadResponse(originalName, storedName, url);
    }

    // 업로드 한 문서 조회
    @Transactional(readOnly = true)
    @Override
    public List<DocumentResponseDTO.DocumentListItemResponse> getUserDocuments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus._USER_NOT_FOUND));

        List<DocumentFile> documents = documentRepository.findAllByUserId(userId);

        return documents.stream()
                .map(doc -> DocumentResponseDTO.DocumentListItemResponse.builder()
                        .documentId(doc.getId())
                        .originalName(doc.getOriginalName())
                        .storedName(doc.getStoredName())
                        .url(doc.getUrl()) // 필요 시 presigned URL 생성
                        .createdAt(doc.getCreatedAt())
                        .build())
                .toList();
    }

    // 업로드 한 서류 다운로드
    @Transactional(readOnly = true)
    @Override
    public DocumentResponseDTO.DocumentDownloadResponse downloadDocument(Long userId, Long documentId) {
        DocumentFile document = documentRepository.findByIdAndUserId(documentId, userId)
                .orElseThrow(() -> new DocumentException(ErrorStatus.DOCUMENT_DOWNLOAD_FAILED));

        // 👉 MinioService 통해 Presigned URL 생성
        String presignedUrl = minioService.getPresignedUrl(document.getStoredName(), 60 * 60);

        return UserConverter.toDocumentDownloadResponse(document, presignedUrl);
    }


    // 업로드 한 서류 삭제
    @Transactional
    @Override
    public void deleteDocument(Long userId, Long documentId) {
        // 1. 유저 확인 + 문서 조회
        DocumentFile document = documentRepository.findByIdAndUserId(documentId, userId)
                .orElseThrow(() -> new DocumentException(ErrorStatus.DOCUMENT_NOT_FOUND));

        // 2. MinIO에서 삭제
        minioService.deleteFile(document.getStoredName());

        // 3. DB에서 삭제
        documentRepository.delete(document);
    }


}
