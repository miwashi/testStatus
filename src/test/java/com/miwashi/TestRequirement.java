package com.miwashi;

import com.miwashi.model.Requirement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = TestStatusApplication.class)
//@WebAppConfiguration
public class TestRequirement {

    @Test
    public void testRequriement() throws Exception{
        Requirement requirement = new Requirement("se.svt.test.svtse.frontpage.GlobalAutocompleteTest.autoCompleteNoResultShouldShowUp");

        System.out.println(requirement.getTestRequirement());
    }

    @Test
    public void testSubject() throws Exception{
        Requirement requirement = new Requirement("se.svt.test.svtse.frontpage.GlobalAutocompleteTest.autoCompleteNoResultShouldShowUp");

        System.out.println(requirement.getTestSubject());
    }

    @Test
    public void testGroup() throws Exception{
        Requirement requirement = new Requirement("se.svt.test.svtse.frontpage.GlobalAutocompleteTest.autoCompleteNoResultShouldShowUp");

        System.out.println(requirement.getTestGroup());
    }

    @Test
    public void testSubGroup() throws Exception{
        Requirement requirement = new Requirement("se.svt.test.svtse.frontpage.GlobalAutocompleteTest.autoCompleteNoResultShouldShowUp");

        System.out.println(requirement.getTestSubGroup());
    }

    @Test
    public void testSubjectKey() throws Exception{
        Requirement requirement = new Requirement("se.svt.test.svtse.frontpage.GlobalAutocompleteTest.autoCompleteNoResultShouldShowUp");

        System.out.println("Subject key: " + requirement.getTestSubjectKey());
    }
}
