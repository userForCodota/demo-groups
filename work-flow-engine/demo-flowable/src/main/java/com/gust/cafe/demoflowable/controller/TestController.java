package com.gust.cafe.demoflowable.controller;

import com.gust.cafe.demoflowable.bean.MyBean;
import org.flowable.common.engine.impl.test.CleanTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请输入类描述
 *
 * @author Dororo
 * @date 2024-06-14 09:24
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private MyBean myBean;

    @GetMapping("/test")
    public String test() {
        return myBean.getAssignee();
    }
}
