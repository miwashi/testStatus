package com.miwashi;

import java.io.FileReader;
import java.util.Iterator;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.miwashi.model.Job;
import com.miwashi.repositories.JobRepository;

import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
    JobRepository jobRepository;
	
	@Value("${configuration.initialdata:false}")
    private boolean do_init_data = false;
	
	/**
	 * This method is called during Spring's startup.
	 * 
	 * @param event Event raised when an ApplicationContext gets initialized or
	 * refreshed.
	 **/
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		if(!do_init_data) return;
		
		loadHistoricalData();
		return;
	}

	private void loadHistoricalData(){
		
		@SuppressWarnings("deprecation")
		JSONParser parser = new JSONParser();
		 
        try {
            Object obj = parser.parse(new FileReader("./src/test/resources/json/jobs.json"));
            JSONArray jobList = (JSONArray) obj;
 
            //String name = (String) jsonObject.get("Name");
            Iterator<Object> iterator = jobList.iterator();
            while (iterator.hasNext()) {
            	try{
	            	JSONObject job = (JSONObject) iterator.next();
	            	job.toJSONString();
	            	org.json.JSONObject aJsonJob = new org.json.JSONObject(job.toJSONString());
	            	jobRepository.save(new Job(aJsonJob));
            	}catch(Throwable ignore){
            		System.out.println(ignore);
            	}
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
