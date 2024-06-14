package com.gust.cafe.demoflowable;

import cn.hutool.system.SystemUtil;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoFlowableApplicationTests2 {
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
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/001_test/example01.bpmn20.xml")
                .name("任务分配案例")
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
        String processDefinitionId = "example01:1:6f914002-29ee-11ef-9dfc-005056c00001";
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId);
    }

    /**
     * 查询用户待办事项
     */
    @Test
    void findFlow() {
        TaskService taskService = processEngine.getTaskService();
        // 获取act_ru_task 表中 assignee 字段是zhangsan的全部记录
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("zhangsan")
                .list();
        for (Task task : list) {
            System.out.println(task.getId());
        }
    }

    @Test
    void completeTask() {
        TaskService taskService = processEngine.getTaskService();
        // 完成任务,可以理解为完成某个节点,ID来自`act_ru_task`.`ID_`
        Map<String, Object> variables = new HashMap<>();
        variables.put("myAssign1", "lisi");
        taskService.complete("af2cdc7a-29ef-11ef-8dc2-005056c00001", variables);
    }
}
