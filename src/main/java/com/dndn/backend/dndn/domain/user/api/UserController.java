package com.dndn.backend.dndn.domain.user.api;

import com.dndn.backend.dndn.domain.user.application.DocumentService;
import com.dndn.backend.dndn.domain.user.domain.entity.Disabled;
import com.dndn.backend.dndn.domain.user.domain.entity.Senior;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.user.dto.*;
import com.dndn.backend.dndn.domain.user.service.UserService;
import com.dndn.backend.dndn.global.common.response.BaseResponse;
import com.dndn.backend.dndn.global.config.security.auth.UserPrincipal;
import com.dndn.backend.dndn.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final DocumentService documentService;

    // 신규 사용자 정보 입력
    @PostMapping
    @Operation(
            summary = "신규 사용자 정보 등록",
            description = "로그인 후 생성 된 jwt accesstoken을 헤더에 삽입하고, 사용자 정보는 requestbody를 통해 넘겨주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "USER_4001", description = "유저가 존재하지 않습니다.")
    })
    public BaseResponse<UserResponseDTO> createUser(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody @Valid UserRequestDTO dto) {
        return BaseResponse.onSuccess(
                SuccessStatus.OK,
                UserResponseDTO.from(userService.createUser(dto, principal.getId()))
        );
    }

    @GetMapping("/{user-id}")
    @Operation(summary = "사용자 정보 불러오기", description = "사용자의 기본 정보를 불러옵니다.")
    public BaseResponse<UserResponseDTO> getUserInfo(@PathVariable("user-id") Long userId) {

        User user= userService.getUserById(userId);
        return BaseResponse.onSuccess(
                SuccessStatus.OK,
                UserResponseDTO.from(user));

    }


    @PostMapping("/{user-id}/senior")
    @Operation(summary = "추가정보(노인)등록", description = "노인의 추가 정보를 등록합니다.")
    public BaseResponse<SeniorResponseDTO> registerSeniorInfo(
            @PathVariable("user-id") Long userId,
            @RequestBody SeniorRequestDTO dto) {

        Senior senior = userService.registerSeniorInfo(userId, dto);

        return BaseResponse.onSuccess(
                SuccessStatus.OK,
                SeniorResponseDTO.from(senior));
    }


    @PostMapping("/{user-id}/disabled")
    @Operation(summary = "추가정보(장애인) 등록", description = "장애인의 기본 정보를 등록합니다.")
    public BaseResponse<DisabledResponseDTO> registerDisabledInfo(
            @PathVariable("user-id") Long userId,
            @RequestBody DisabledRequestDTO dto) {

        Disabled info= userService.registerDisabledInfo(userId, dto);
        return BaseResponse.onSuccess(
                SuccessStatus.OK,
                DisabledResponseDTO.from(info));
    }

    @GetMapping("/{user-id}/senior")
    @Operation(summary = "노인 정보 불러오기", description = "노인의 기본 정보를 불러옵니다.")
    public BaseResponse<SeniorResponseDTO> getSeniorInfo(@PathVariable("user-id") Long userId) {

        Senior senior = userService.getSeniorInfo(userId);
        return BaseResponse.onSuccess(
                SuccessStatus.OK,
                SeniorResponseDTO.from(senior));


    }

    @GetMapping("/{user-id}/disabled")
    @Operation(summary = "장애인 정보 불러오기", description = "장애인의 기본 정보를 불러옵니다.")
    public BaseResponse<DisabledResponseDTO> getDisabledInfo(@PathVariable("user-id") Long userId) {

        Disabled info = userService.getDisabledInfo(userId);
        return BaseResponse.onSuccess(
                SuccessStatus.OK,
                DisabledResponseDTO.from(info));
    }

    @PutMapping("/{user-id}")
    @Operation (summary = "사용자 정보 수정하기", description = "사용자 정보를 수정합니다.")
    public BaseResponse<UserResponseDTO> updateUserInfo(
            @PathVariable("user-id") Long userId,
            @RequestBody @Valid UserUpdateRequestDTO dto) {

        User updatedUser= userService.updateUser(userId, dto);
        return BaseResponse.onSuccess(
                SuccessStatus.OK,
                UserResponseDTO.from(updatedUser));
    }


    // 서류 파일 업로드
    @PostMapping(
            value = "/documents/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "서류 파일 업로드",
            description = "jwt 토큰 인증 후 파일 업로드 하세요."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "USER_4001", description = "유저가 존재하지 않습니다."),
            @ApiResponse(responseCode = "DOCUMENT_4001", description = "파일 업로드에 실패했습니다."),
            @ApiResponse(responseCode = "DOCUMENT_201", description = "파일을 업로드에 성공했습니다.")
    })
    public BaseResponse<DocumentResponseDTO.DocumentUploadResponse> uploadDocument(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestPart("file") MultipartFile file) {

        DocumentResponseDTO.DocumentUploadResponse result = documentService.uploadFile(file, principal.getId());

        return BaseResponse.onSuccess(SuccessStatus.DOCUMENT_UPLOAD_SUCCESS, result);
    }

    // 업로드 한 서류 목록 조회
    @GetMapping("/documents")
    @Operation(
            summary = "내 문서 목록 조회",
            description = "jwt 토큰 인증 후 조회하세요."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "USER_4001", description = "유저가 존재하지 않습니다."),
            @ApiResponse(responseCode = "DOCUMENT_200", description = "파일 목록 조회가 성공적으로 완료되었습니다.")
    })
    public BaseResponse<List<DocumentResponseDTO.DocumentListItemResponse>> getUserDocuments(
            @AuthenticationPrincipal UserPrincipal principal) {

        List<DocumentResponseDTO.DocumentListItemResponse> result =
                documentService.getUserDocuments(principal.getId());

        return BaseResponse.onSuccess(SuccessStatus.DOCUMENT_LIST_SUCCESS, result);
    }

    // 서류 파일 다운로드
    @GetMapping("/documents/{document-id}/download")
    @Operation(
            summary = "문서 다운로드",
            description = "jwt 토큰 인증 후 path로 document-id를 넘겨주세요."
    )
    public ResponseEntity<InputStreamResource> downloadDocument(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("document-id") Long documentId) {

        return documentService.downloadDocument(principal.getId(), documentId);
    }

    // 서류 파일 삭제
    @DeleteMapping("/documents/{document-id}")
    @Operation(summary = "문서 삭제", description = "해당 유저가 업로드한 특정 문서를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "USER_4001", description = "유저가 존재하지 않습니다."),
            @ApiResponse(responseCode = "DOCUMENT_4002", description = "파일 삭제에 실패했습니다."),
            @ApiResponse(responseCode = "DOCUMENT_204", description = "파일 삭제가 성공적으로 완료되었습니다.")
    })
    public BaseResponse<Void> deleteDocument(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("document-id") Long documentId) {

        documentService.deleteDocument(principal.getId(), documentId);

        return BaseResponse.onSuccess(SuccessStatus.DOCUMENT_DELETE_SUCCESS, null);
    }

}