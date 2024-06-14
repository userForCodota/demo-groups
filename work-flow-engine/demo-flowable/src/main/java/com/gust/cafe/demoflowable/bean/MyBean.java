package com.gust.cafe.demoflowable.bean;

import org.springframework.stereotype.Component;

@Component
public class MyBean {
    public String getAssignee() {
        System.out.println("getAssignee() called with parameters NULL");
        return "gust";
    }
}
