package com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class CentralListResDto {

    private WantedList wantedList;

    @Getter
    public static class WantedList {

        private String totalCount;
        private String pageNo;
        private String numOfRows;
        private String resultCode;
        private String resultMessage;

        // 서비스 목록 (복수)
        private List<ServiceItem> servList;
    }

    @Getter
    public static class ServiceItem {

        private String inqNum;
        private String intrsThemaArray;
        private String jurMnofNm;
        private String jurOrgNm;
        private String lifeArray;
        private String onapPsbltYn;
        private String rprsCtadr;
        private String servDgst;
        private String servDtlLink;
        private String servId;
        private String servNm;
        private String sprtCycNm;
        private String srvPvsnNm;
        private String svcfrstRegTs;
        private String trgterIndvdlArray;
    }
}
