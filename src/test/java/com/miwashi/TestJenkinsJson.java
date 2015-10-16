package com.miwashi;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.miwashi.model.transients.jenkins.Job;
import com.miwashi.model.transients.jenkins.JobCause;
import com.miwashi.model.transients.jenkins.JobChangeSet;
import com.miwashi.model.transients.jenkins.JobResult;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import net.minidev.json.JSONArray;

public class TestJenkinsJson {

	static private Log logger = LogFactory.getLog(TestJenkinsJson.class);
	
	OkHttpClient client = new OkHttpClient();

    String doGetRequest(String url) throws IOException {
      Request request = new Request.Builder()
          .url(url)
          .build();

      Response response = client.newCall(request).execute();
      return response.body().string();
    }
      
	@Test
	public void test() throws Exception{
		List<String> resultList = null;
		String jsonSelector ="";
		
		String url = "http://localhost:8080/job/svtse-integrationtest/30/api/json";
		String json = doGetRequest(url);
		
		//System.out.println(extractTestResult(json));
		//System.out.println(extractCause(json));
		//System.out.println(extractTimeStamp(json));
		List<JobChangeSet> changes = extractChangeSets(json);
		for(JobChangeSet change : changes){
			System.out.println(change);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Job job = null;
		try{
			job = mapper.readValue(json, Job.class);
		}catch(Exception ignore){
			logger.debug("Couldn't create job!",ignore);
		}
	}
	
	private JobResult extractTestResult(String json){
		Configuration conf = Configuration.defaultConfiguration();
		conf = conf.mappingProvider(new JacksonMappingProvider());
		
		JobResult result = null;
		try{
			List<Map<String, Object>> actions = JsonPath.using(conf).parse(json).read("$.actions.[?((@.failCount)&&(@.failCount)&&(@.totalCount))]", List.class);
			result = JsonPath.using(conf).parse(actions).read("$.[0]", JobResult.class);
		}catch(Throwable ignore){
			logger.debug("Couldn't create JobResult!",ignore);
		}
		return result;
	}
	
	private JobCause extractCause(String json){
		Configuration conf = Configuration.defaultConfiguration();
		//conf = conf.mappingProvider(new JacksonMappingProvider());
		
		JobCause result = null;
		try{
			List<Map<String, Object>> causes = JsonPath.using(conf).parse(json).read("$.actions.[?(@.causes)]", List.class);
			result = JsonPath.using(conf).parse(causes).read("$.[0].causes.[0]", JobCause.class);			
		}catch(Throwable ignore){
			logger.debug("Couldn't find JobCause!",ignore);
		}
		return result;
	}
	
	private Date extractTimeStamp(String json){
		Date date = null;
		try {
			date = JsonPath.parse(json).read("$.timestamps", Date.class);
		} catch(Exception ignore){
			logger.debug("Couldn't find timestamp!",ignore);
		}
		return date;
	}
	
	private String extractFullDisplayName(String json){
		String name = JsonPath.parse(json).read("$.fullDisplayName", String.class);
		Date date = null;
		try {
			name = JsonPath.parse(json).read("$.fullDisplayName", String.class);
		} catch(Exception ignore){
			logger.debug("Couldn't find name!",ignore);
		}
		return name;
	}
	
	private List<JobChangeSet> extractChangeSets(String json){
		List<JobChangeSet> changes = new ArrayList<JobChangeSet>();
		Configuration conf = Configuration.defaultConfiguration();
		conf = conf.mappingProvider(new JacksonMappingProvider());
		
		try{
			List<Map<String, Object>> items = JsonPath.using(conf).parse(json).read("$.changeSet.items.*", List.class);
			for(Object item : items){
				JobChangeSet changeSet = JsonPath.using(conf).parse(item).read("$", JobChangeSet.class);
				if(changeSet!=null){
					changes.add(changeSet);
				}
			}						
		}catch(Throwable ignore){
			logger.debug("Couldn't find changesets!",ignore);
		}
		return changes;
	}

}
