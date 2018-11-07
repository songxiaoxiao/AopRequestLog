package com.web.core.app.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@RequestMapping("/index")
public class IndexController {

    @GetMapping
    @ResponseBody
    public String index(){
        return "Hello";
    }
}
