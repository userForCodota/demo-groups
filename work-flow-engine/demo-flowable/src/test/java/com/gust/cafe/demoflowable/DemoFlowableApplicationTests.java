package com.gust.cafe.demoflowable;

import cn.hutool.system.SystemUtil;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoFlowableApplicationTests {
    @Autowired
    private ProcessEngine processEngine;
    // @Autowired
    // private RepositoryService repositoryService;
    // @Autowired
    // private RuntimeService runtimeService;


    /**
     * 部署
     */
    @Test
    void contextLoads() {
        String javaHome = SystemUtil.get("JAVA_HOME");
        System.out.println(javaHome);
        // 打印
        System.out.println(processEngine);
        // 后续步骤
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/001_test/firstflow.bpmn20.xml")
                .name("my_first_flow")
                .deploy();
        String deploymentId = deployment.getId();
        System.out.println("部署ID：" + deploymentId);
    }

    /**
     * 启动流程实例
     */
    @Test
    void startup() {
        // 也可以直接注入
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 两种方式启动流程实例
        // 根据KEY,来自`act_re_procdef`.`KEY_` ,需要自己维护唯一性
        // String processDefinitionKey = "firstflow";
        // runtimeService.startProcessInstanceByKey(processDefinitionKey);

        // 根据ID（推荐）,来自`act_re_procdef`.`ID_`
        String processDefinitionId = "firstflow:1:ba3f555a-2950-11ef-ad58-005056c00001";
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId);
    }
}
