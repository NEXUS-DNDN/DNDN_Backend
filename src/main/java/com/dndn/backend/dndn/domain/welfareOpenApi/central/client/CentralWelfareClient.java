package com.dndn.backend.dndn.domain.welfareOpenApi.central.client;

import com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response.CentralDetailResDto;
import com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response.CentralListResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
                .scheme("https")
                .host(baseUrl)
                .path("/oasis/openapi/service/rest/WlfareService/getWlfareList")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("pageNo", page)
                .queryParam("numOfRows", numOfRows)
                .queryParam("_type", "json")
                .build()
                .toUriString();

        return restTemplate.getForObject(url, CentralListResDto.class);
    }

    public CentralDetailResDto getWelfareDetail(String servId) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(baseUrl)
                .path("/oasis/openapi/service/rest/WlfareService/getWlfareInfo")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("servId", servId)
                .queryParam("_type", "json")
                .build()
                .toUriString();

        return restTemplate.getForObject(url, CentralDetailResDto.class);
    }
}



