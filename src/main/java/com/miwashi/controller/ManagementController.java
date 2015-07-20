package com.miwashi.controller;

import com.miwashi.model.Setting;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.*;

@ConfigurationProperties
@RestController
public class ManagementController {

    private Environment env = null;

    @Autowired
    void setEnvrironment(Environment env){
        this.env = env;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    @ApiOperation(value = "/settings", notes = "Settins of system")
    public ModelAndView hello() {
        ModelAndView mav = new ModelAndView("settings");
        mav.addObject("title","Settings");

        System.out.println(env.toString());

        List<Setting> settings = new ArrayList<Setting>();
        Properties properties = System.getProperties();
        properties.forEach((key, value) -> {
            settings.add(new Setting((String) key, (String) value));
        });
        Collections.sort(settings);
        mav.addObject("settings", settings);

        List<Setting> profiles = new ArrayList<Setting>();
        String envSettings = env.toString();
        StringTokenizer aToknizer = new StringTokenizer(envSettings,",");
        while(aToknizer.hasMoreTokens()) {
            String row = aToknizer.nextToken();
            String key = ".";
            String value = row;
            if(row.indexOf("=")>0) {
                key = row.substring(0, row.indexOf("="));
                value = row.substring(row.indexOf("=") + 1, row.length());
            }
            profiles.add(new Setting(key, value));
        }
        mav.addObject("profiles", profiles);


        return mav;
    }

}
