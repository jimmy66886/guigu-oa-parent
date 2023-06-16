package com.zzmr.auth.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ProcessTestDemo2 {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    public void deployProcess() {
        Deployment deploy = repositoryService.createDeployment().name("加班")
                .addClasspathResource("process/jiaban01.bpmn20.xml").deploy();
        System.out.println(deploy.getName());
        System.out.println(deploy.getId());
    }

    @Test
    public void startProcess() {

        Map<String, Object> map = new HashMap<>();
        map.put("assignee1", "lucy02");
        map.put("assignee2", "mary02");


        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban01", map);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }

    @Test
    public void findTask() {
        List<Task> list = taskService.createTaskQuery().taskAssignee("zhao").list();
        for (Task task : list) {
            System.out.println("流程实例id: " + task.getProcessInstanceId());
            System.out.println("任务id: " + task.getId());
            System.out.println("任务负责人: " + task.getAssignee());
            System.out.println("任务名称: " + task.getName());
        }
    }

    //=====================

    @Test
    public void startProcess1() {

        Map<String, Object> map = new HashMap<>();
        map.put("assignee1", "lucy03");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban01", map);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }

    @Test
    public void completeTask() {
        Task task = taskService.createTaskQuery().taskAssignee("lucy03").singleResult();
        Map<String, Object> variables = new HashMap<>();
        variables.put("assignee2", "zhao");
        taskService.complete(task.getId(), variables);
    }


}
