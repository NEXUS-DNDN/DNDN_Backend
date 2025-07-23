package com.nexus.dndn.dndn_server;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeployTestController {

    @GetMapping("/")
    public String testDeploy() {
        return "<h1>CI/CD 파이프라인이 정상적으로 작동 중입니다.</h1>";
    }
}
