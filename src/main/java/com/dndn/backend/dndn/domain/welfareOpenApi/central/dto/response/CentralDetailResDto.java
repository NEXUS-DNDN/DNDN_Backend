package com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response;

import lombok.Getter;
import java.util.List;

@Getter
public class CentralDetailResDto {

    private WantedDtl wantedDtl;

    @Getter
    public static class WantedDtl {

        private String servId;
        private String servNm;
        private String jurMnofNm;
        private String tgtrDtlCn;
        private String slctCritCn;
        private String alwServCn;
        private String crtrYr;
        private String rprsCtadr;
        private String wlfareInfoOutlCn;
        private String sprtCycNm;
        private String srvPvsnNm;
        private String lifeArray;
        private String trgterIndvdlArray;
        private String intrsThemaArray;

        private List<ServDetail> applmetList;
        private List<ServDetail> inqplCtadrList;
        private List<ServDetail> inqplHmpgReldList;
        private List<ServDetail> basfrmList;
        private List<ServLaw> baslawList;

        private String resultCode;
        private String resultMessage;
    }

    @Getter
    public static class ServDetail {
        private String servSeCode;
        private String servSeDetailLink;
        private String servSeDetailNm;
    }

    @Getter
    public static class ServLaw {
        private String servSeCode;
        private String servSeDetailNm;
    }
}
