package com.dndn.backend.dndn.domain.user.api;

import com.dndn.backend.dndn.domain.user.domain.entity.Disabled;
import com.dndn.backend.dndn.domain.user.domain.entity.Senior;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.user.dto.*;
import com.dndn.backend.dndn.domain.user.service.UserService;
import com.dndn.backend.dndn.global.common.response.BaseResponse;
import com.dndn.backend.dndn.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "사용자 등록", description = "사용자의 기본 정보를 등록합니다.")
    public BaseResponse<UserResponseDTO> createUser(@RequestBody UserRequestDTO dto) {
        return BaseResponse.onSuccess(
                SuccessStatus.OK,
                UserResponseDTO.from(userService.createUser(dto))
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

//    @GetMapping("/{user-id}/senior")
//    public BaseResponse<SeniorResponseDTO> getSeniorInfo(@PathVariable("user-id") Long userId) {
//        Senior senior = userService.getSeniorInfo(userId);
//        return BaseResponse.onSuccess(SuccessStatus.OK, SeniorResponseDTO.from(senior));
//
//
//    }
//
//    @GetMapping("/{user-id}/disabled")
//    public BaseResponse<DisabledResponseDTO> getDisabledInfo(@PathVariable("user-id") Long userId) {
//        Disabled info = userService.getDisabledInfo(userId);
//        return BaseResponse.onSuccess(SuccessStatus.OK, DisabledResponseDTO.from(info));
//    }




}
