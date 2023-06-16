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
public class ProcessTestDemo3 {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    public void deployProcess() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban04.bpmn20.xml").name("加班04").deploy();
        System.out.println(deploy.getName());
        System.out.println(deploy.getId());
    }

    @Test
    public void startProcess1() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban04");
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }

    // 查询组任务
    @Test
    public void findGroupTaskList() {
        List<Task> list = taskService.createTaskQuery().taskCandidateUser("tom01").list();
        for (Task task : list) {
            System.out.println("流程实例id: " + task.getProcessInstanceId());
            System.out.println("任务id: " + task.getId());
            System.out.println("任务负责人: " + task.getAssignee());
            System.out.println("任务名称: " + task.getName());
        }
    }

    // 拾取组任务
    @Test
    public void claimTask() {
        Task task = taskService.createTaskQuery().taskCandidateUser("tom01").singleResult();
        if (task != null) {
            taskService.claim(task.getId(), "tom01");
            System.out.println("==========");
            System.out.println("任务拾取完成");
            // 如果张三01失去了任务，张三02就不能拾取了
        }
    }


    @Test
    public void findTask() {
        List<Task> list = taskService.createTaskQuery().taskAssignee("tom01").list();
        for (Task task : list) {
            System.out.println("流程实例id: " + task.getProcessInstanceId());
            System.out.println("任务id: " + task.getId());
            System.out.println("任务负责人: " + task.getAssignee());
            System.out.println("任务名称: " + task.getName());
        }
    }

    @Test
    public void completeTask() {
        Task task = taskService.createTaskQuery().taskAssignee("tom01").singleResult();
        taskService.complete(task.getId());
    }

    // 归还组任务-如果个人不想办理该任务，可以归还组任务，归还后该用户不再是该任务的负责人

    // 任务交接


}
