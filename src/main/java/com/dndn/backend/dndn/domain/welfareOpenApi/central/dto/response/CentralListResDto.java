package com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

import java.util.List;

@JacksonXmlRootElement(localName = "wantedList")
@Getter
public class CentralListResDto {

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
    public static class ServiceItem {

        @JacksonXmlProperty(localName = "inqNum")
        private String inqNum;

        @JacksonXmlProperty(localName = "intrsThemaArray")
        private String intrsThemaArray;

        @JacksonXmlProperty(localName = "jurMnofNm")
        private String jurMnofNm;

        @JacksonXmlProperty(localName = "jurOrgNm")
        private String jurOrgNm;

        @JacksonXmlProperty(localName = "lifeArray")
        private String lifeArray;

        @JacksonXmlProperty(localName = "onapPsbltYn")
        private String onapPsbltYn;

        @JacksonXmlProperty(localName = "rprsCtadr")
        private String rprsCtadr;

        @JacksonXmlProperty(localName = "servDgst")
        private String servDgst;

        @JacksonXmlProperty(localName = "servDtlLink")
        private String servDtlLink;

        @JacksonXmlProperty(localName = "servId")
        private String servId;

        @JacksonXmlProperty(localName = "servNm")
        private String servNm;

        @JacksonXmlProperty(localName = "sprtCycNm")
        private String sprtCycNm;

        @JacksonXmlProperty(localName = "srvPvsnNm")
        private String srvPvsnNm;

        @JacksonXmlProperty(localName = "svcfrstRegTs")
        private String svcfrstRegTs;

        @JacksonXmlProperty(localName = "trgterIndvdlArray")
        private String trgterIndvdlArray;
    }
}
