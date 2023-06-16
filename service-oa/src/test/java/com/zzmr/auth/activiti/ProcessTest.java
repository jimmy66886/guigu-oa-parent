package com.zzmr.auth.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProcessTest {

    // 负责流程部署
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    // 单个流程实例挂起
    @Test
    public void singleSuspendProcessInstance() {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId("69896e53-06dc-11ee-83eb-106fd9b0b368").singleResult();
        boolean suspended = processInstance.isSuspended();
        if (suspended) {
            runtimeService.activateProcessInstanceById("69896e53-06dc-11ee-83eb-106fd9b0b368");
            System.out.println("69896e53-06dc-11ee-83eb-106fd9b0b368" + " 激活了");
        } else {
            runtimeService.suspendProcessInstanceById("69896e53-06dc-11ee-83eb-106fd9b0b368");
            System.out.println("69896e53-06dc-11ee-83eb-106fd9b0b368" + " 挂起了");
        }
    }


    // 全部流程实例挂起
    @Test
    public void suspendProcessInstanceAll() {
        // 1. 获取流程定义的对象
        ProcessDefinition qingjia = repositoryService.createProcessDefinitionQuery().processDefinitionKey("qingjia").singleResult();
        // 2. 调用流程定义对象的方法判断当前的状态,挂起,激活
        boolean suspended = qingjia.isSuspended();
        // 3. 如果是挂起,就实现激活
        if (suspended) {
            // 流程定义的id/是否激活/时间点
            repositoryService.activateProcessDefinitionById(qingjia.getId(), true, null);
            System.out.println(qingjia.getId() + " 激活了");
        } else {
            // 4. 如果是激活,就实现挂起
            repositoryService.suspendProcessDefinitionById(qingjia.getId(), true, null);
            System.out.println(qingjia.getId() + " 挂起了");
        }


    }

    // 创建流程实例,指定BusinessKey
    @Test
    public void startUpProcessAddBusinessKey() {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("qingjia", "1001");
        System.out.println(instance.getBusinessKey());
        System.out.println(instance.getId());
    }

    @Test
    public void findCompletedTask() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("lisi").finished().list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("实例id: " + historicTaskInstance.getProcessInstanceId());
            System.out.println("任务id: " + historicTaskInstance.getId());
            System.out.println("任务负责人: " + historicTaskInstance.getAssignee());
            System.out.println("任务名称: " + historicTaskInstance.getName());
        }
    }

    // 处理当前任务
    @Test
    public void completeTask() {
        // 查询负责人需要处理的任务有哪些
        Task task = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();
        // 处理这个任务,参数是任务id
        taskService.complete(task.getId());
    }

    // 查询个人的待办任务--张三
    @Test
    public void findTaskList() {
        String assign = "zhangsan";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assign).list();
        for (Task task : list) {
            System.out.println("流程实例id: " + task.getProcessInstanceId());
            System.out.println("任务id: " + task.getId());
            System.out.println("任务负责人: " + task.getAssignee());
            System.out.println("任务名称: " + task.getName());
        }
    }


    // 启动流程实例
    @Test
    public void startProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia");
        System.out.println("流程定义的id: " + processInstance.getProcessDefinitionId());
        System.out.println("流程实例的id: " + processInstance.getId());
        System.out.println("流程活动的id: " + processInstance.getActivityId());
    }

    // 单个文件的部署
    @Test
    public void deployProcess() {

        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia.bpmn20.xml")
                .addClasspathResource("process/qingjia.png")
                .name("请假申请流程").deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());

    }


}
