package com.dianying.task;

import org.springframework.stereotype.Component;

/**
 * author: ysk13
 * date: 2017-5-22
 * description: 定时任务
 */
@Component
public class TaskDemo {
    // @Scheduled(cron = "*/5 * * * * ? ") // 间隔5秒执行
    public void taskCycle() {
        System.out.println("SpringMVC框架配置的定时任务");
    }
}
