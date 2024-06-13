package com.gust.cafe.demoflowable;

import cn.hutool.system.SystemUtil;
import org.flowable.engine.ProcessEngine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoFlowableApplicationTests {
    @Autowired
    private ProcessEngine processEngine;

    @Test
    void contextLoads() {
        String javaHome = SystemUtil.get("JAVA_HOME");
        System.out.println(javaHome);
        // 打印
        System.out.println(processEngine);
    }
}
