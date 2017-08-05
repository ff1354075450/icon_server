package com.dianying.init;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * author: ysk13
 * date: 2017-5-22
 * description: 启动时自动执行
 */
@Component
public class Init implements ApplicationListener<ContextRefreshedEvent> {
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("启动完后执行该方法");
        System.out.println("测试新建分支");
    }
}
