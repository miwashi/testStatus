package com.miwashi.controller;

import com.miwashi.model.Requirement;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.FileNotFoundException;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String hello() throws FileNotFoundException {
        return "hello worlds";
    }

    @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "/test/{id}", notes = "Returns a status")
    public Requirement getRequirement(@PathParam(value = "Path id") @PathVariable final String id) {
        System.out.println("Got " + id + " from path!");
        return new Requirement();
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ApiOperation(value = "/test/{id}", notes = "Returns a status")
    public Requirement getRequirement(@ApiParam(value = "Parameter id") @RequestParam(required = false, defaultValue = "0") final Long id) {
        System.out.println("Got " + id + " from id param!");
        return new Requirement();
    }

}
