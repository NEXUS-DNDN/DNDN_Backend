package com.dndn.backend.dndn.domain.welfareOpenApi.central.client;

import com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response.CentralDetailResDto;
import com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response.CentralListResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class CentralWelfareClient {

    private final RestTemplate restTemplate;

    @Value("${openapi.central.base-url}")
    private String baseUrl;

    @Value("${openapi.central.service-key}")
    private String serviceKey;

    // 키를 '한 번만' 인코딩
    private String encodeServiceKeyOnce(String k){
        k = k == null ? "" : k.trim();
        if (k.contains("%")) return k; // 이미 인코딩된 키면 그대로
        return UriUtils.encodeQueryParam(k, StandardCharsets.UTF_8);
    }

    public CentralListResDto getWelfareList(int page, int numOfRows) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(baseUrl)
                .path("/B554287/NationalWelfareInformationsV001/NationalWelfarelistV001")
                .queryParam("serviceKey", serviceKey)
                .queryParam("callTp", "L")
                .queryParam("pageNo", page)
                .queryParam("numOfRows", numOfRows)
                .queryParam("srchKeyCode", "003")
                .build(false)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<CentralListResDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CentralListResDto.class
        );

        return response.getBody();
    }

    public CentralDetailResDto getWelfareDetail(String servId) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(baseUrl)
                .path("/B554287/NationalWelfareInformationsV001/NationalWelfaredetailV001")
                .queryParam("serviceKey", serviceKey)
                .queryParam("callTp", "D")
                .queryParam("servId", servId)
                .build(false)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<CentralDetailResDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CentralDetailResDto.class
        );

        return response.getBody();
    }


    // 테스트용
    public String debugWelfareListXml(int page, int numOfRows) {
        String encodedKey = encodeServiceKeyOnce(serviceKey);

        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(baseUrl) // "apis.data.go.kr"
                .path("/B554287/NationalWelfareInformationsV001/NationalWelfarelistV001")
                .queryParam("serviceKey", encodedKey) // 디코딩 키면 OK
                .queryParam("callTp", "L")
                .queryParam("pageNo", page)
                .queryParam("numOfRows", numOfRows)
                .queryParam("srchKeyCode", "001")
                .build(false)
                .toUriString();

        log.info("최종 URL = {}", url);
        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class);
        log.info("Raw XML:\n{}", res.getBody());
        return res.getBody();
    }

}
