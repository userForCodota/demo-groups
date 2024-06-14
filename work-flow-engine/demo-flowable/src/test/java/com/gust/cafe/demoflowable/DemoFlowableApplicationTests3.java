package com.gust.cafe.demoflowable;

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
class DemoFlowableApplicationTests3 {
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
                .addClasspathResource("process/001_test/example02.bpmn20.xml")
                .name("任务分配案例_监听器案例")
                .deploy();
        String deploymentId = deployment.getId();
        // ID来自`act_re_deployment`.`ID_`
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
        String processDefinitionId = "example02:1:26965384-29ff-11ef-b597-005056c00001";
        // runtimeService.startProcessInstanceById(processDefinitionId);
        // 如果第一个节点的审批人也是表达式,则同理需要传入参数
        Map<String, Object> variables = new HashMap<>();
        variables.put("assignee2", "zhangsan");
        runtimeService.startProcessInstanceById(processDefinitionId, variables);
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
        // variables.put("myAssign1", "lisi");
        // taskService.complete("e8ac6dea-29f7-11ef-b361-005056c00001", variables);
        taskService.complete("9894a06f-29f8-11ef-955b-005056c00001");
    }
}
