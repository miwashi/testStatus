package com.miwashi;

import com.miwashi.model.Requirement;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
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

        //System.out.println(requirement.getTestSubject());
    }

    @Test
    public void testGroup() throws Exception{
        Requirement requirement = new Requirement("se.svt.test.svtse.frontpage.GlobalAutocompleteTest.autoCompleteNoResultShouldShowUp");

        //System.out.println(requirement.getTestGroup());
    }

    @Test
    public void testSubGroup() throws Exception{
        Requirement requirement = new Requirement("se.svt.test.svtse.frontpage.GlobalAutocompleteTest.autoCompleteNoResultShouldShowUp");

        //System.out.println(requirement.getTestSubGroup());
    }

    @Test
    public void testSubjectKey() throws Exception{
        Requirement requirement = new Requirement("se.svt.test.svtse.frontpage.GlobalAutocompleteTest.autoCompleteNoResultShouldShowUp");

        //System.out.println("Subject key: " + requirement.getTestSubjectKey());
    }
    
    @Test
    public void testReadJson(){
    	String jsonText = "";
    	String filename = "./src/main/resources/static/json/requirement.json";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            jsonText = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        try{

        	Map<String, String> data = new HashMap<String, String>();
	        JSONArray jar = new JSONArray(jsonText);
	        
	        int rows = 0;
	        for(int i=0 ; i < jar.length(); i++){
	        	JSONObject jobj = jar.getJSONObject(i);
	        	JSONArray results = jobj.getJSONArray("results");
	        	for(int j=0 ; j < results.length(); j++){
	        		JSONObject result = results.getJSONObject(j);
	        		String jobName = jobj.getString("name");
	        		String status = result.getBoolean("status")?"SUCCESS":"FAIL";
	        		
	        		JSONObject browser = result.getJSONObject("browser");
	        		String browserName = browser.getString("name");
	        		JSONObject platform = result.getJSONObject("platform");
	        		String platformName = platform.getString("name");
	        		
	        		String value = ",{\"" + jobName+ "\",\"" + status + "\",";
	        		value+= "\"" + browserName + "\"";
	        		value+=",\"" + platformName + "\"}";
	        		
	        		String key = jobName + status + browserName + platformName;
	        		data.put(key, value);
	        		
	        		rows++;
	        	}
	        }
	        FileWriter fileWriter = new FileWriter("udpdata.txt");
	        for(String value : data.values()){
	        	fileWriter.write(value + "\n");
	        }
	        fileWriter.close();
        	System.out.println(rows);
	       
        }catch(Throwable e){
        	System.out.println(e);
        }
    }
}
