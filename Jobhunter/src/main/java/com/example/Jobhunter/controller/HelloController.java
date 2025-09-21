package com.example.Jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Jobhunter.util.error.IdInvalidException;

@RestController
public class HelloController {

    @GetMapping("/")
    public String sayHello() throws IdInvalidException{
        if(true) {
            throw new IdInvalidException("Dang test exception");
        }
        return "Hello, Jobhunter!";
    }

}
