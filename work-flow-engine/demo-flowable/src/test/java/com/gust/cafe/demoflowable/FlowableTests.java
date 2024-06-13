package com.gust.cafe.demoflowable;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.junit.jupiter.api.Test;

public class FlowableTests {
    @Test
    public void deploy() {
        System.out.println("开始部署...");
        // 配置
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration();
        // CREATE DATABASE `flowable_test` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */
        cfg.setJdbcUrl("jdbc:mysql://localhost:3306/flowable_test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true&allowMultiQueries=true");
        cfg.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        cfg.setJdbcUsername("root");
        cfg.setJdbcPassword("root");
        cfg.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        // 构建
        ProcessEngine processEngine = cfg.buildProcessEngine();
        // 部署
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/001_test/firstflow.bpmn20.xml")
                .name("my_first_flow")
                .deploy();
        String deploymentId = deployment.getId();
        System.out.println("部署ID：" + deploymentId);

        System.out.println("结束部署");
    }
}
