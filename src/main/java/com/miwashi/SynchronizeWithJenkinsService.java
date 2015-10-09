package com.miwashi;

import static com.miwashi.util.StaticUtil.getJsonField;
import static com.miwashi.util.StaticUtil.getJsonFieldAsInt;
import static com.miwashi.util.StaticUtil.getJsonFieldAsArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.miwashi.model.Job;
import com.miwashi.repositories.JobRepository;
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

    @Autowired
    JobRepository jobRepository;

    @Scheduled(fixedRateString = "${configuration.schedule.synchwithjenkins.rate}")
    public void synchronize(){
        Iterable<Job> jobsIterable = jobRepository.findAll();
        for(Job job : jobsIterable){
        	if(!"unknown".equalsIgnoreCase(job.getJenkinsDuration())){
        		//continue;
        	}
        	String url = job.getJobStatusUrl();
        	try {
				JSONObject obj = readJsonFromUrl(url);
				
				job.setJenkinsResult(getJsonField(obj, "result"));
				job.setJenkinsDuration("" + getJsonFieldAsInt(obj, "duration"));
				
				String foo = "";
				foo = "" + getJsonFieldAsInt(obj, "estimatedDuration");
				foo = getJsonField(obj, "executor");
				foo = getJsonField(obj, "fullDisplayName");
				foo = "" + getJsonFieldAsInt(obj, "timestamp");
				JSONArray actions = getJsonFieldAsArray(obj, "actions");
				
				for(int i=0; i < actions.length(); i++){
					JSONObject actionObj = actions.getJSONObject(i);
					if(actionObj.has("totalCount") && actionObj.has("failCount")){
						job.setJenkinsFailCount(getJsonFieldAsInt(actionObj, "failCount"));
						job.setJenkinsSkipCount(getJsonFieldAsInt(actionObj, "skipCount"));
						job.setJenkinsTotalCount(getJsonFieldAsInt(actionObj, "totalCount"));
						job.setJenkinstTestReportUrl(job.getBuildUrl() + "/" + getJsonField(actionObj, "testReport"));
					}
				}
				jobRepository.save(job);
				
			} catch (JSONException | IOException e) {
				//Ignore for now
				//System.out.println("Faild to load json from " + url);
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
