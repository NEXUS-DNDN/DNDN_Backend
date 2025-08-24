package com.dndn.backend.dndn.global.service;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final DefaultMessageService messageService;

    @Value("${coolsms.from}")
    private String from;

    public void sendVerificationCode(String to, String code) {
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText("[DNDN] 인증번호는 " + code + " 입니다. (3분 이내 입력)");

        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println("SMS 발송 결과: " + response);
    }

}
