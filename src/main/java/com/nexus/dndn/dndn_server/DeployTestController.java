package com.nexus.dndn.dndn_server;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeployTestController {

    @GetMapping("/")
    public String testDeploy() {
        return "<h3>배포 테스트 중입니다</h3>";
    }
}
