package com.miwashi.controller;

import com.miwashi.model.Requirement;
import com.wordnik.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.FileNotFoundException;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello worlds";
    }

}
