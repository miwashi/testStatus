package com.miwashi;

import com.miwashi.model.Job;
import com.miwashi.model.Requirement;
import com.miwashi.model.Result;
import com.miwashi.repositories.JobRepository;
import com.miwashi.repositories.RequirementRepository;
import com.miwashi.repositories.ResultRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


@Service
public class SynchronizeWithJenkinsService {

    private Log log = LogFactory.getLog(SynchronizeWithJenkinsService.class);

    @Autowired
    JobRepository jobRepository;

    @Scheduled(fixedRateString = "${configuration.schedule.synchwithjenkins.rate}")
    public void synchronize(){
        Iterable<Job> jobsIterable = jobRepository.findAll();
        for(Job job : jobsIterable){
        	String url = url = job.getBuildUrl() + "api/json";
        	try {
				JSONObject obj = readJsonFromUrl(url);
				if(obj.has("result")){
					job.setJenkinsResult(obj.getString("result"));
				}
				
				if(obj.has("duration")){
					job.setJenkinsDuration("" + obj.getInt("duration"));
				}
				jobRepository.save(job);
				
			} catch (JSONException | IOException e) {
				//Ignore for now
				System.out.println("Faild to load json from " + url);
			}
        }
    }
    
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
          BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
          String jsonText = readAll(rd);
          JSONObject json = new JSONObject(jsonText);
          return json;
        } finally {
          is.close();
        }
      }
    
    private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }
}
