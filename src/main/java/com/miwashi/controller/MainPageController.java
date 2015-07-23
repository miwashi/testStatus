package com.miwashi.controller;

import com.miwashi.TestStatusApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainPageController {

    @RequestMapping("/")
    public ModelAndView hello() {
        ModelAndView mav = new ModelAndView("index");

        long numberOfTests = TestStatusApplication.getNumberOfTests();
        long numberOfCompletedTests = TestStatusApplication.getNumberOfCompletedTests();
        long numberOfFailedTests = TestStatusApplication.getNumberOfFailedTests();

        mav.addObject("numberOfTestsToday","" + TestStatusApplication.getNumberOfTestsToday());
        mav.addObject("numberOfCompletedTestsToday","" + TestStatusApplication.getNumberOfCompletedTestsToday());
        mav.addObject("numberOfFailedTestsToday","" + TestStatusApplication.getNumberOfFailedTestsToday());

        mav.addObject("numberOfTestsLast24Hours","" + TestStatusApplication.getNumberOfTestsLast24Hours());
        mav.addObject("numberOfCompletedTestsLast24Hours","" + TestStatusApplication.getNumberOfCompletedTestsLast24Hours());
        mav.addObject("numberOfFailedTestsLast24Hours","" + TestStatusApplication.getNumberOfFailedTestsLast24Hours());

        mav.addObject("numberOfTestsLastHour","" + TestStatusApplication.getNumberOfTestsLastHour());
        mav.addObject("numberOfCompletedTestsLastHour","" + TestStatusApplication.getNumberOfCompletedTestsLastHour());
        mav.addObject("numberOfFailedTestsLastHour","" + TestStatusApplication.getNumberOfFailedTestsLastHour());

        mav.addObject("numberOfTestsLastWeek","" + TestStatusApplication.getNumberOfTestsLastWeek());
        mav.addObject("numberOfCompletedTestsLastWeek","" + TestStatusApplication.getNumberOfCompletedTestsLastWeek());
        mav.addObject("numberOfFailedTestsLastWeek","" + TestStatusApplication.getNumberOfFailedTestsLastWeek());

        mav.addObject("numberOfTests","" + TestStatusApplication.getNumberOfTests());
        mav.addObject("numberOfCompletedTests","" + TestStatusApplication.getNumberOfCompletedTests());
        mav.addObject("numberOfFailedTests","" + TestStatusApplication.getNumberOfFailedTests());

        return mav;
    }

}
