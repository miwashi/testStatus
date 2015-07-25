package com.miwashi.controller;

import com.miwashi.TestStatusApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainPageController {

    @RequestMapping("/")
    public ModelAndView hello() {
        ModelAndView mav = new ModelAndView("index");

        mav.addObject("stats", TestStatusApplication.getStats());

        return mav;
    }

}
