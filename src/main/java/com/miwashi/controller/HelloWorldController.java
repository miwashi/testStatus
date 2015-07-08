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
    public String hello() throws FileNotFoundException {
        return "hello worlds";
    }

    @RequestMapping(value = "/requirement/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/requirement/{id}", notes = "Returns a status")
    public Requirement getRequirement(@PathParam(value = "Path id") @PathVariable final String id) {
        System.out.println("Got " + id + " from path!");
        return new Requirement();
    }

    @RequestMapping(value = "/requirement", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/requirement", notes = "Returns a status", response = Requirement.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ok")
            ,@ApiResponse(code = 404, message = "missing")
    })
    public Requirement getRequirement(@ApiParam(value = "Parameter id") @RequestParam(required = false, defaultValue = "0") final Long id) {
        System.out.println("Got " + id + " from id param!");
        return new Requirement();
    }

}
