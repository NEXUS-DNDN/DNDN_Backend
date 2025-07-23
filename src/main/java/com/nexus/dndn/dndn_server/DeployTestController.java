package com.nexus.dndn.dndn_server;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeployTestController {

    @GetMapping("/")
    public String testDeploy() {
        return "<h2>DNDN 서버에 오신 것을 환영합니다!</h2>";
    }
}
