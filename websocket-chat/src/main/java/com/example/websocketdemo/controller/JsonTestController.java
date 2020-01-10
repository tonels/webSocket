package com.example.websocketdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonTestController {

    @GetMapping("/index")
    public String index() {
        return "testWebsocket";
    }
//
//    @GetMapping("/")
//    public String index2() {
//        return "testWebsocket";
//    }
}
