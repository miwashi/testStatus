package net.miwashi.teststatus.controller;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.ApiOperation;

import net.miwashi.teststatus.model.Requirement;
import net.miwashi.teststatus.model.Result;
import net.miwashi.teststatus.model.Setting;
import net.miwashi.teststatus.repositories.BrowserRepository;
import net.miwashi.teststatus.repositories.FailRepository;
import net.miwashi.teststatus.repositories.GroupRepository;
import net.miwashi.teststatus.repositories.JobRepository;
import net.miwashi.teststatus.repositories.PlatformRepository;
import net.miwashi.teststatus.repositories.RequirementRepository;
import net.miwashi.teststatus.repositories.ResultRepository;
import net.miwashi.teststatus.repositories.SubjectRepository;

@CrossOrigin
@ConfigurationProperties
@RestController
public class ManagementController {
	
	private Log log = LogFactory.getLog(ManagementController.class);

    private Environment env = null;
    
    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    ResultRepository resultRepository;

    @Autowired
    FailRepository failRepository;
    
    @Autowired
    BrowserRepository browserRepository;

    @Autowired
    PlatformRepository platformRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    SubjectRepository subjectRepository;
    
    @Autowired
    JobRepository jobRepository;

    @Autowired
    void setEnvrironment(Environment env){
        this.env = env;
    }

    @RequestMapping(value = "/backup", method = RequestMethod.GET)
    @ApiOperation(value = "/backup", notes = "Settins of system")
    public ModelAndView dump(){
    	ModelAndView mav = new ModelAndView("dump");
    	
    	Iterable<Requirement> requirementIter = requirementRepository.findAll();
    	Collection<Requirement> requirements = new ArrayList<Requirement>();
    	for(Requirement requirement : requirementIter){
    		requirements.add(requirement);
    	}
    	save(requirements, "requirements");
    	
    	Iterable<Result> resultIter = resultRepository.findAll();
    	Collection<Result> results = new ArrayList<Result>();
    	for(Result result : resultIter){
    		results.add(result);
    	}
    	save(results, "result");
    	
    	return mav;
    }
    
    @RequestMapping(value = "/restore", method = RequestMethod.GET)
    @ApiOperation(value = "/restore", notes = "Settins of system")
    public ModelAndView restore(){
    	ModelAndView mav = new ModelAndView("dump");
    	
    	ObjectMapper mapper = new ObjectMapper();
		String json;
		try {
			json = new String(readAllBytes(get("./src/test/resources/dump/requirements.json")));
			List<Requirement> requirements = mapper.readValue(json, new TypeReference<List<Requirement>>(){});
			for(Requirement requirement: requirements){
				System.out.println(requirement.getKey());
				requirementRepository.save(requirement);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mav;
    }
    
    private void save(Collection data, String entity){
    	ObjectMapper mapper = new ObjectMapper();
    	try {
			String json = mapper.writeValueAsString(data);
			Files.write(Paths.get("./src/test/resources/dump/" + entity + ".json"), json.getBytes());
		} catch (JsonProcessingException e) {
			log.warn(e);
		} catch (IOException e) {
			log.warn(e);
		}
    }
    
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    @ApiOperation(value = "/settings", notes = "Settins of system")
    public ModelAndView settings() {
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

        List<Setting> envVars = new ArrayList<Setting>();
        Map<String, String> systemEnv = System.getenv();
        for (String envName : systemEnv.keySet()) {
            envVars.add(new Setting(envName, systemEnv.get(envName)));
        }
        mav.addObject("envs", envVars);

        List<Setting> appProperties = new ArrayList<Setting>();
        appProperties.add(getPropertyAsSetting("spring.profile"));
        appProperties.add(getPropertyAsSetting("spring.profiles"));
        appProperties.add(getPropertyAsSetting("configuration.projectName"));
        appProperties.add(getPropertyAsSetting("configuration.default.browser"));
        appProperties.add(getPropertyAsSetting("configuration.default.platform"));
        appProperties.add(getPropertyAsSetting("configuration.log.receivedresult"));
        appProperties.add(getPropertyAsSetting("configuration.log.sendtestdata"));
        appProperties.add(getPropertyAsSetting("configuration.testdata"));
        mav.addObject("appProperties", appProperties);

        return mav;
    }

    private Setting getPropertyAsSetting(String propertyName){
        return new Setting(propertyName, env.getProperty(propertyName));
    }

}
