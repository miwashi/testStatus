package com.miwashi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.miwashi.model.CheckinComment;
import com.miwashi.model.Job;
import com.miwashi.model.transients.jenkins.JobCause;
import com.miwashi.model.transients.jenkins.JobChangeSet;
import com.miwashi.model.transients.jenkins.JobResult;
import com.miwashi.repositories.JobRepository;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
//Progressbar
//http://localhost:8080/job/test-util/lastBuild/api/xml?depth=1&xpath=*/executor/progress/text()
/**
 * http://localhost:8080//job/test-util/api/json?tree=builds[number,status,timestamp,id,result]
 * http://localhost:8080/job/test-util/lastBuild/api/json
 * http://localhost:8080//job/test-util/lastBuild/api/json?tree=result,timestamp,estimatedDuration
 * 
 * http://localhost:8080/computer/(master)/api/json
 * 
 * https://testjenkins.svti.svt.se/job/teamtest-bolibompa/1/testReport/api/json
 * 
 * http://nssjenkins.svti.svt.se:8080/job/news-regressiontests/lastCompletedBuild/testReport/api/json
 * 
 * Host name must be set in jenkins!
 * 
 * http://nssjenkins.svti.svt.se:8080/job/news-regressiontests/497/testReport/api/json
 * 
 * @author miwa01
 *
 */
@Service
public class SynchronizeWithJenkinsService {

    private Log log = LogFactory.getLog(SynchronizeWithJenkinsService.class);
    private OkHttpClient client = new OkHttpClient();

    @Autowired
    JobRepository jobRepository;

    @Scheduled(fixedRateString = "${configuration.schedule.synchwithjenkins.rate}")
    public void synchronize(){
        Iterable<Job> jobsIterable = jobRepository.findAll();
        for(Job job : jobsIterable){
        	if(job.getJenkinsDuration()>0){
        		continue;
        	}
        	synchronizeJob(job);
        }
    }
    
    private void synchronizeJob(Job job){
    	String url = job.getJobStatusUrl();
    	String json = readJsonFromUrl(url);
    	if(json==null || json.isEmpty()){
    		return;
    	}
    	com.miwashi.model.transients.jenkins.Job jenkinsJob = jsonToJob(json);
    	if(jenkinsJob==null || jenkinsJob.getId() == null || jenkinsJob.getId().isEmpty()){
    		return;
    	}
    	JobResult jobResult = jsonToJobResult(json);
    	//JobCause jobCause = jsonToJobCause(json);
    	List<JobChangeSet> changes = toChangeSets(json);
    	
    	if(jobResult!=null){
	    	job.setJenkinsDuration(jenkinsJob.getDuration());
	    	job.setJenkinsFailCount(jobResult.getFailCount());
	    	job.setJenkinsPassCount(jobResult.getTotalCount() - jobResult.getFailCount() - jobResult.getSkipCount());
	    	job.setJenkinsTotalCount(jobResult.getTotalCount());
	    	job.setJenkinsSkipCount(jobResult.getSkipCount());
	    	job.setJenkinstTestReportUrl(job.getJenkinsUrl() + "/" + jobResult.getUrlName());
    	}
    	
    	for(JobChangeSet change : changes){
    		CheckinComment comment = new CheckinComment();
    		comment.setAuthor(change.getAuthor().getFullName());
    		comment.setComment(change.getComment());
    	}
    	jobRepository.save(job);
    }
    
    private String readJsonFromUrl(String url) {
    	Request request = new Request.Builder().url(url).build();
    	Response response;
    	String result = "";
		try {
			response = client.newCall(request).execute();
			result = response.body().string();
		} catch (IOException e) {
			log.warn("Failed to load jenkins json.", e);
		}
    	return result;
  }
    
    private com.miwashi.model.transients.jenkins.Job jsonToJob(String json){
    	ObjectMapper mapper = new ObjectMapper();
    	com.miwashi.model.transients.jenkins.Job job = null;
		try{
			job = mapper.readValue(json, com.miwashi.model.transients.jenkins.Job.class);
		}catch(Exception e){
			log.warn("Failed to create job from json.", e);
		}
		return job;
    }
    
    private JobResult jsonToJobResult(String json){
		Configuration conf = Configuration.defaultConfiguration();
		conf = conf.mappingProvider(new JacksonMappingProvider());
		
		JobResult result = null;
		try{
			List<Map<String, Object>> actions = JsonPath.using(conf).parse(json).read("$.actions.[?((@.failCount)&&(@.failCount)&&(@.totalCount))]", List.class);
			result = JsonPath.using(conf).parse(actions).read("$.[0]", JobResult.class);
		}catch(Throwable ignore){
			//log.debug("Couldn't create JobResult!",ignore);
		}
		return result;
	}
	
	private JobCause jsonToJobCause(String json){
		Configuration conf = Configuration.defaultConfiguration();
		//conf = conf.mappingProvider(new JacksonMappingProvider());
		conf.addOptions(new Option[]{Option.SUPPRESS_EXCEPTIONS});
		JobCause result = null;
		try{
			List<Map<String, Object>> causes = JsonPath.using(conf).parse(json).read("$.actions.[?(@.causes)]", List.class);
			result = JsonPath.using(conf).parse(causes).read("$.[0].causes.[0]", JobCause.class);			
		}catch(Throwable ignore){
			//log.debug("Couldn't find JobCause!",ignore);
		}
		return result;
	}
	
	private List<JobChangeSet> toChangeSets(String json){
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
			//log.debug("Couldn't find changesets!",ignore);
		}
		return changes;
	}
}
