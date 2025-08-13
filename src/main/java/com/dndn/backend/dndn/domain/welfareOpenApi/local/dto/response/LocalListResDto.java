package com.dndn.backend.dndn.domain.welfareOpenApi.local.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "wantedList")
public class LocalListResDto {
    @JacksonXmlProperty(localName = "totalCount")
    private String totalCount;

    @JacksonXmlProperty(localName = "pageNo")
    private String pageNo;

    @JacksonXmlProperty(localName = "numOfRows")
    private String numOfRows;

    @JacksonXmlProperty(localName = "resultCode")
    private String resultCode;

    @JacksonXmlProperty(localName = "resultMessage")
    private String resultMessage;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "servList")
    private List<ServiceItem> servList;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServiceItem {

        @JacksonXmlProperty(localName = "aplyMtdNm")
        private String aplyMtdNm;

        @JacksonXmlProperty(localName = "bizChrDeptNm")
        private String bizChrDeptNm;

        @JacksonXmlProperty(localName = "ctpvNm")
        private String ctpvNm;

        @JacksonXmlProperty(localName = "inqNum")
        private String inqNum;

        @JacksonXmlProperty(localName = "intrsThemaNmArray")
        private String intrsThemaNmArray;

        @JacksonXmlProperty(localName = "lastModYmd")
        private String lastModYmd;

        @JacksonXmlProperty(localName = "lifeNmArray")
        private String lifeNmArray;

        @JacksonXmlProperty(localName = "servDgst")
        private String servDgst;

        @JacksonXmlProperty(localName = "servDtlLink")
        private String servDtlLink;

        @JacksonXmlProperty(localName = "servId")
        private String servId;

        @JacksonXmlProperty(localName = "servNm")
        private String servNm;

        @JacksonXmlProperty(localName = "sggNm")
        private String sggNm;

        @JacksonXmlProperty(localName = "sprtCycNm")
        private String sprtCycNm;

        @JacksonXmlProperty(localName = "srvPvsnNm")
        private String srvPvsnNm;

        @JacksonXmlProperty(localName = "trgterIndvdlNmArray")
        private String trgterIndvdlNmArray;
    }
}
