package com.miwashi.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class JenkinsResult {
	
	private int age = 0;
	private String className = "";
	private double duration = 0;
	private String errorDetails = "";
	private String errorStackTrace = "";
	private int failedSince = 0;
	private String name = "";
	private boolean skipped = false;
	private String skippedMessage = "";
	private String status = "";
	private String stderr = "";
	private String stdout = "";
	
	private Double jobDuration = 0.0;
	private boolean jobEmpty = false;
	private int jobFailCount=0;
	private int jobPassCount=0;
	private int jobSkipCount=0;
		
	public JenkinsResult(){
		super();
	}
	
	public JenkinsResult(JSONObject testCase){
		age = parseInt("age", testCase);
		className = parseString("className", testCase);
		duration = parseDouble("duration", testCase);
		errorDetails = parseString("errorDetails", testCase);
		errorStackTrace = parseString("errorStackTrace", testCase);
		failedSince = parseInt("failedSince", testCase);
		name = parseString("name", testCase);
		skipped = parseBoolean("skipped", testCase);
		skippedMessage = parseString("skippedMessage", testCase);
		status = parseString("status", testCase);
		stderr = parseString("stderr", testCase);
		stdout = parseString("stdout", testCase);
	}
	
	
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public String getErrorStackTrace() {
		return errorStackTrace;
	}

	public void setErrorStackTrace(String errorStackTrace) {
		this.errorStackTrace = errorStackTrace;
	}

	public int getFailedSince() {
		return failedSince;
	}

	public void setFailedSince(int failedSince) {
		this.failedSince = failedSince;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSkipped() {
		return skipped;
	}

	public void setSkipped(boolean skipped) {
		this.skipped = skipped;
	}

	public String getSkippedMessage() {
		return skippedMessage;
	}

	public void setSkippedMessage(String skippedMessage) {
		this.skippedMessage = skippedMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStderr() {
		return stderr;
	}

	public void setStderr(String stderr) {
		this.stderr = stderr;
	}

	public String getStdout() {
		return stdout;
	}

	public void setStdout(String stdout) {
		this.stdout = stdout;
	}

	
	
	public Double getJobDuration() {
		return jobDuration;
	}

	public void setJobDuration(Double jobDuration) {
		this.jobDuration = jobDuration;
	}

	public boolean isJobEmpty() {
		return jobEmpty;
	}

	public void setJobEmpty(boolean jobEmpty) {
		this.jobEmpty = jobEmpty;
	}

	public int getJobFailCount() {
		return jobFailCount;
	}

	public void setJobFailCount(int jobFailCount) {
		this.jobFailCount = jobFailCount;
	}

	public int getJobPassCount() {
		return jobPassCount;
	}

	public void setJobPassCount(int jobPassCount) {
		this.jobPassCount = jobPassCount;
	}

	public int getJobSkipCount() {
		return jobSkipCount;
	}

	public void setJobSkipCount(int jobSkipCount) {
		this.jobSkipCount = jobSkipCount;
	}

	private static String parseString(String field, JSONObject object){
		String result = "";
		
		if(object.has(field) && !object.isNull(field)){
			result = object.getString(field); 
		}
		return result;
	}
	
	private static boolean parseBoolean(String field, JSONObject object){
		boolean result = false;
		if(object.has(field)){
			result = object.getBoolean(field); 
		}
		return result;
	}
	
	private static Double parseDouble(String field, JSONObject object){
		Double result = 0.0;
		if(object.has(field)){
			result = object.getDouble(field); 
		}
		return result;
	}
	
	private static int parseInt(String field, JSONObject object){
		Integer result = 0;
		if(object.has(field)){
			result = object.getInt(field); 
		}
		return result;
	}
	
	public static JenkinsResult parseResultFor(JSONObject obj, String requirementKey){
		JenkinsResult result = null;
		if(obj.has("suites")){
			JSONArray suites = obj.getJSONArray("suites");
			for(int i= 0; i< suites.length(); i++){
				JSONObject suite = suites.getJSONObject(i);
				if(suite.has("cases") && suite.has("name")){
					JSONArray cases = suite.getJSONArray("cases");
					for(int j= 0; j< cases.length(); j++){
						JSONObject testCase = cases.getJSONObject(j);
						if(testCase.has("name") && testCase.has("className")){
							String thisCaseKey = testCase.get("className") + "." + testCase.get("name"); 
							if(thisCaseKey!=null && thisCaseKey.equalsIgnoreCase(requirementKey)){
								result = new JenkinsResult(testCase);
								
								result.setJobDuration(parseDouble("duration", testCase));
								result.setJobEmpty(parseBoolean("empty", testCase));
								result.setJobFailCount(parseInt("failCount", testCase));
								result.setJobPassCount(parseInt("passCount", testCase));
								result.setJobSkipCount(parseInt("skipCount", testCase));
								return result;
							}
						}
					}
				}
			}
			
		}
		return result;
	}
}
