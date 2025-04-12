package com.happy_hao.rums;

import com.happy_hao.rums.config.FeishuConfig;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RumsApplicationTests {

    @Resource
    private FeishuConfig feishuConfig;

    @Test
    void contextLoads() {
        System.out.println(feishuConfig.getRedirectUri());
    }

}
