package com.popit.popitproject.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping    // 기본 홈 경로
    public String index() {
        return "jenkins Test28";
    }

}
