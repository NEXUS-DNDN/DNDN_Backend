package com.dndn.backend.dndn.domain.welfareOpenApi.local.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "wantedDtl")
public class LocalDetailResDto {
    @JacksonXmlProperty(localName = "resultCode")
    private String resultCode;

    @JacksonXmlProperty(localName = "resultMessage")
    private String resultMessage;

    @JacksonXmlProperty(localName = "aplyDocList")
    private String aplyDocList;

    @JacksonXmlProperty(localName = "servId")
    private String servId;

    @JacksonXmlProperty(localName = "servNm")
    private String servNm;

    @JacksonXmlProperty(localName = "enfcBgngYmd")
    private String enfcBgngYmd;

    @JacksonXmlProperty(localName = "enfcEndYmd")
    private String enfcEndYmd;

    @JacksonXmlProperty(localName = "bizChrDeptNm")
    private String bizChrDeptNm;

    @JacksonXmlProperty(localName = "ctpvNm")
    private String ctpvNm;

    @JacksonXmlProperty(localName = "sggNm")
    private String sggNm;

    @JacksonXmlProperty(localName = "servDgst")
    private String servDgst;

    @JacksonXmlProperty(localName = "lifeNmArray")
    private String lifeNmArray;

    @JacksonXmlProperty(localName = "trgterIndvdlNmArray")
    private String trgterIndvdlNmArray;

    @JacksonXmlProperty(localName = "intrsThemaNmArray")
    private String intrsThemaNmArray;

    @JacksonXmlProperty(localName = "sprtCycNm")
    private String sprtCycNm;

    @JacksonXmlProperty(localName = "srvPvsnNm")
    private String srvPvsnNm;

    @JacksonXmlProperty(localName = "aplyMtdNm")
    private String aplyMtdNm;

    @JacksonXmlProperty(localName = "sprtTrgtCn")
    private String sprtTrgtCn;

    @JacksonXmlProperty(localName = "slctCritCn")
    private String slctCritCn;

    @JacksonXmlProperty(localName = "alwServCn")
    private String alwServCn;

    @JacksonXmlProperty(localName = "aplyMtdCn")
    private String aplyMtdCn;

    @JacksonXmlProperty(localName = "inqNum")
    private String inqNum;

    @JacksonXmlProperty(localName = "lastModYmd")
    private String lastModYmd;

    @JacksonXmlProperty(localName = "inqplCtadrList")
    private RelatedInfo inqplCtadrList;

    @JacksonXmlProperty(localName = "inqplHmpgReldList")
    private RelatedInfo inqplHmpgReldList;

    @JacksonXmlProperty(localName = "baslawList")
    private RelatedInfo baslawList;

    @JacksonXmlProperty(localName = "basfrmList")
    private RelatedInfo basfrmList;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RelatedInfo {

        @JacksonXmlProperty(localName = "wlfareInfoDtlCd")
        private String wlfareInfoDtlCd;

        @JacksonXmlProperty(localName = "wlfareInfoReldNm")
        private String wlfareInfoReldNm;

        @JacksonXmlProperty(localName = "wlfareInfoReldCn")
        private String wlfareInfoReldCn;
    }
}
