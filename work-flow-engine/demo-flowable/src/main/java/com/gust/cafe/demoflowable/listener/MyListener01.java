package com.gust.cafe.demoflowable.listener;


import cn.hutool.core.util.StrUtil;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;

public class MyListener01 implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String className = this.getClass().getSimpleName();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println(StrUtil.format("[{}]-[{}]-----执行了[{}]", className, methodName, delegateTask.getName()));
        // 判断事件类型
        if (EVENTNAME_CREATE.equals(delegateTask.getEventName())) {
            // 任务节点创建回调,这里的动作是指派任务负责人
            delegateTask.setAssignee("dororo");
        }
    }
}
