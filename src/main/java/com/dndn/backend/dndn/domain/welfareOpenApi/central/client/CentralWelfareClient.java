package com.dndn.backend.dndn.domain.welfareOpenApi.central.client;

import com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response.CentralDetailResDto;
import com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response.CentralListResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class CentralWelfareClient {

    private final RestTemplate restTemplate;

    @Value("${openapi.central.base-url}")
    private String baseUrl;

    @Value("${openapi.central.service-key}")
    private String serviceKey;

    public CentralListResDto getWelfareList(int page, int numOfRows) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(baseUrl)
                .path("/B554287/NationalWelfareInformationsV001/NationalWelfarelistV001")
                .queryParam("serviceKey", serviceKey)
                .queryParam("callTp", "L")
                .queryParam("pageNo", page)
                .queryParam("numOfRows", numOfRows)
                .queryParam("srchKeyCode", "003")
                .build()
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
                .scheme("http")
                .host(baseUrl)
                .path("/B554287/NationalWelfareInformationsV001/NationalWelfaredetailV001")
                .queryParam("serviceKey", serviceKey)
                .queryParam("callTp", "D")
                .queryParam("servId", servId)
                .build()
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
        String rawUrl = "http://apis.data.go.kr/B554287/NationalWelfareInformationsV001/NationalWelfarelistV001"
                + "?serviceKey=ak8Ud2Tri7EB6Z+R2xErOr0KsxAZxsqL93NEj/5lbXcvfTPNvozAnr7hYwF7kRlMmV/d60SYzqbpdC260aWBZg=="
                + "&callTp=L"
                + "&pageNo=1"
                + "&numOfRows=10"
                + "&srchKeyCode=001";

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                rawUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        System.out.println("✅ Raw XML 응답: \n" + response.getBody()); // 👈 여기가 핵심
        return response.getBody();
    }

}
