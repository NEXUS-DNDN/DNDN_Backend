package com.nexus.dndn.dndn_server;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeployTestController {

    @GetMapping("/")
    public String testDeploy() {
        return "<h1>CI/CD 정상 작동</h1>";
    }
}
