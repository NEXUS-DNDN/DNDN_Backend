package com.nexus.dndn.dndn_server;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeployTestController {

    @GetMapping("/")
    public String testDeploy() {
        return "<h3>든든(DNDN) 서버에 오신 것을 환영합니다!</h1><p>CI/CD 파이프라인이 정상적으로 작동하고 있습니다.</h3>";
    }
}
