package com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

import java.util.List;

@Getter
@JacksonXmlRootElement(localName = "wantedDtl")
public class CentralDetailResDto {

    @JacksonXmlProperty(localName = "servId")
    private String servId;

    @JacksonXmlProperty(localName = "servNm")
    private String servNm;

    @JacksonXmlProperty(localName = "jurMnofNm")
    private String jurMnofNm;

    @JacksonXmlProperty(localName = "jurOrgNm")
    private String jurOrgNm;

    @JacksonXmlProperty(localName = "tgtrDtlCn")
    private String tgtrDtlCn;

    @JacksonXmlProperty(localName = "slctCritCn")
    private String slctCritCn;

    @JacksonXmlProperty(localName = "alwServCn")
    private String alwServCn;

    @JacksonXmlProperty(localName = "crtrYr")
    private String crtrYr;

    @JacksonXmlProperty(localName = "rprsCtadr")
    private String rprsCtadr;

    @JacksonXmlProperty(localName = "wlfareInfoOutlCn")
    private String wlfareInfoOutlCn;

    @JacksonXmlProperty(localName = "sprtCycNm")
    private String sprtCycNm;

    @JacksonXmlProperty(localName = "srvPvsnNm")
    private String srvPvsnNm;

    @JacksonXmlProperty(localName = "lifeArray")
    private String lifeArray;

    @JacksonXmlProperty(localName = "trgterIndvdlArray")
    private String trgterIndvdlArray;

    @JacksonXmlProperty(localName = "intrsThemaArray")
    private String intrsThemaArray;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "applmetList")
    private List<ServDetail> applmetList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "inqplCtadrList")
    private List<ServDetail> inqplCtadrList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "inqplHmpgReldList")
    private List<ServDetail> inqplHmpgReldList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "basfrmList")
    private List<ServDetail> basfrmList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "baslawList")
    private List<ServLaw> baslawList;

    @JacksonXmlProperty(localName = "resultCode")
    private String resultCode;

    @JacksonXmlProperty(localName = "resultMessage")
    private String resultMessage;

    @Getter
    public static class ServDetail {

        @JacksonXmlProperty(localName = "servSeCode")
        private String servSeCode;

        @JacksonXmlProperty(localName = "servSeDetailLink")
        private String servSeDetailLink;

        @JacksonXmlProperty(localName = "servSeDetailNm")
        private String servSeDetailNm;
    }

    @Getter
    public static class ServLaw {

        @JacksonXmlProperty(localName = "servSeCode")
        private String servSeCode;

        @JacksonXmlProperty(localName = "servSeDetailNm")
        private String servSeDetailNm;
    }
}
