package com.miwashi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String hello() throws FileNotFoundException {

        return "hello worlds";
    }

}
