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

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class CentralWelfareClient {

    private final RestTemplate restTemplate;

    @Value("${openapi.central.base-url}")   // 예: apis.data.go.kr
    private String baseUrl;

    @Value("${openapi.central.service-key}") // 환경변수의 키(디코딩키 권장)
    private String serviceKey;

    /* ---- 공통 헤더 ---- */
    private HttpEntity<Void> xmlEntity() {
        HttpHeaders h = new HttpHeaders();
        h.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
        h.set(HttpHeaders.ACCEPT_CHARSET, "utf-8");
        h.set(HttpHeaders.CONNECTION, "close");
        h.set(HttpHeaders.USER_AGENT,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        return new HttpEntity<>(h);
    }

    /* ---- URI 빌더 (키 상태에 따라 인코딩 전략 분기) ---- */
    private URI buildListUri(int page, int numOfRows, String srchKeyCode) {
        String rawKey = serviceKey == null ? "" : serviceKey.trim();

        UriComponentsBuilder b = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(baseUrl)
                .path("/B554287/NationalWelfareInformationsV001/NationalWelfarelistV001")
                .queryParam("callTp", "L")
                .queryParam("pageNo", page)
                .queryParam("numOfRows", numOfRows)
                .queryParam("srchKeyCode", srchKeyCode)
                .queryParam("serviceKey", rawKey);

        URI uri = rawKey.contains("%")
                ? b.build(true).toUri()                            // 인코딩키: 재인코딩 금지
                : b.encode(StandardCharsets.UTF_8).build().toUri(); // 디코딩키: 여기서 인코딩

        log.info("[중앙복지] LIST URI = {}", uri);
        return uri;
    }

    private URI buildDetailUri(String servId) {
        String rawKey = serviceKey == null ? "" : serviceKey.trim();

        UriComponentsBuilder b = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(baseUrl)
                // ✅ 상세도 list 엔드포인트 사용!
                .path("/B554287/NationalWelfareInformationsV001/NationalWelfarelistV001")
                .queryParam("callTp", "D")
                .queryParam("servId", servId)
                .queryParam("serviceKey", rawKey);

        URI uri = rawKey.contains("%")
                ? b.build(true).toUri()                            // 인코딩키: 재인코딩 금지
                : b.encode(StandardCharsets.UTF_8).build().toUri(); // 디코딩키: 여기서 인코딩

        log.info("[중앙복지] DETAIL URI = {}", uri);
        return uri;
    }


    /* ---- 실제 호출 ---- */
    public CentralListResDto getWelfareList(int page, int numOfRows) {
        URI uri = buildListUri(page, numOfRows, "003");
        ResponseEntity<CentralListResDto> res = restTemplate.exchange(
                uri, HttpMethod.GET, xmlEntity(), CentralListResDto.class);
        return res.getBody();
    }

    public CentralDetailResDto getWelfareDetail(String servId) {
        URI uri = buildDetailUri(servId);
        ResponseEntity<CentralDetailResDto> res = restTemplate.exchange(
                uri, HttpMethod.GET, xmlEntity(), CentralDetailResDto.class);
        return res.getBody();
    }

    /* ---- 디버그용 Raw XML ---- */
    public String debugWelfareListXml(int page, int numOfRows) {
        URI uri = buildListUri(page, numOfRows, "001");
        String body = restTemplate.exchange(uri, HttpMethod.GET, xmlEntity(), String.class).getBody();
        log.info("[중앙복지] Raw XML:\n{}", body);
        return body;
    }
}
