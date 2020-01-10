package com.github.ws1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 这里 controller 处理返回视图名
 */
@Controller
@RequestMapping
public class IndexController {

    @RequestMapping({"/", "/index"})
    public String index() {
        return "socket";
    }

}
