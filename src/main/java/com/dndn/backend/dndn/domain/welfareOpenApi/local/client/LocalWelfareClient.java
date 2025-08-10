package com.dndn.backend.dndn.domain.welfareOpenApi.local.client;

import com.dndn.backend.dndn.domain.welfareOpenApi.local.dto.response.LocalDetailResDto;
import com.dndn.backend.dndn.domain.welfareOpenApi.local.dto.response.LocalListResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class LocalWelfareClient {
    private final RestTemplate restTemplate;

    @Value("${openapi.local.base-url}")
    private String baseUrl;

    @Value("${openapi.local.service-key}")
    private String serviceKey;

    // 전체 목록 조회
    public LocalListResDto getWelfareList(int page, int numOfRows) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("https") // API가 https로 제공됨
                .host(baseUrl)
                .path("/B554287/LocalGovernmentWelfareInformations/LcgvWelfarelist")
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", page)
                .queryParam("numOfRows", numOfRows)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<LocalListResDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                LocalListResDto.class
        );

        return response.getBody();
    }

    // 상세 조회
    public LocalDetailResDto getWelfareDetail(String servId) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(baseUrl)
                .path("/B554287/LocalGovernmentWelfareInformations/LcgvWelfaredetailed")
                .queryParam("serviceKey", serviceKey)
                .queryParam("servId", servId)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);

        ResponseEntity<LocalDetailResDto> resp = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), LocalDetailResDto.class);

        return resp.getBody();
    }

    /**
     * 테스트용 - Raw XML 출력
     */
    public String debugWelfareListXml(int page, int numOfRows) {
        String rawUrl = "https://apis.data.go.kr/B554287/LocalGovernmentWelfareInformations/LcgvWelfarelist"
                + "?serviceKey=" + serviceKey
                + "&pageNo=" + page
                + "&numOfRows=" + numOfRows;

        HttpEntity<Void> entity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                rawUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        System.out.println("serviceKey 출력: "+ serviceKey);
        System.out.println("✅ Raw XML 응답: \n" + response.getBody());
        return response.getBody();
    }
}
