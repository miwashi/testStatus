package com.miwashi.model;

import org.joda.time.DateTime;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.StringTokenizer;

/**
 * Created by miwa01 on 15-07-16.
 */
public class ResultReport {

    private String size;
    private String type;
    private String uuid;
    private String name;
    private String timeStamp;
    private String status;
    private String browser;
    private String version;
    private String platform;
    private String host;
    private String grid;
    private String user;
    private String buildId;
    private String buildNumber;
    private String jobName;
    private String gitCommit;
    private String gitBranch;
    private String gitURL;
    private String exceptionMessage;
    private DateTime completeTime = null;
    private DateTime startTime;

    public ResultReport(String msg){

        JsonObject json = (new JsonParser()).parse(msg).getAsJsonObject();
        if(!json.has("type")){
            throw new IllegalStateException("Missing type field");
        }
        if(!json.has("name")){
            throw new IllegalStateException("Missing name field");
        }

        StringTokenizer aTokenizer = new StringTokenizer(msg, ";");

        type = json.has("type")?json.get("type").getAsString():"";
        uuid = json.has("uuid")?json.get("uuid").getAsString():"";
        name = json.has("name")?json.get("name").getAsString():"";
        timeStamp = json.has("timeStamp")?json.get("timeStamp").getAsString():"";
        status = json.has("status")?json.get("status").getAsString():"";
        browser = json.has("browser")?json.get("browser").getAsString():"";
        version = json.has("version")?json.get("version").getAsString():"";
        platform = json.has("platform")?json.get("platform").getAsString():"";
        size = json.has("size")?json.get("size").getAsString():"";
        host = json.has("host")?json.get("host").getAsString():"";
        grid = json.has("grid")?json.get("grid").getAsString():"";
        user = json.has("user")?json.get("user").getAsString():"";
        buildId = json.has("buildId")?json.get("buildId").getAsString():"";
        buildNumber = json.has("buildNumber")?json.get("buildNumber").getAsString():"";
        jobName = json.has("jobName")?json.get("jobName").getAsString():"";
        gitCommit = json.has("gitCommit")?json.get("gitCommit").getAsString():"";
        gitBranch = json.has("gitBranch")?json.get("gitBranch").getAsString():"";
        gitURL = json.has("gitURL")?json.get("gitURL").getAsString():"";
        exceptionMessage = json.has("exceptionMessage")?json.get("exceptionMessage").getAsString():"";

        long time = Long.parseLong(timeStamp);
        startTime = new DateTime(time);
    }

    public String toString(){
        String result = "";

        long time = Long.parseLong(timeStamp);

        result += name;
        result += " " + status;
        result += " " + (new DateTime(time)).toString("yyyy hh:mm:ss",null);
        result += " " + timeStamp;
        result += "\n\t" + browser;
        result += "\n\t" + platform;
        result += "\n\t" + host;
        result += "\n\t" + grid;
        result += "\n\t" + user;
        result += "\n\t" + buildId;
        result += "\n\t" + buildNumber;
        result += "\n\t" + jobName;
        result += "\n\t" + gitCommit;
        result += "\n\t" + gitBranch;
        result += "\n\t" + gitURL;
        result += "\n\t" + exceptionMessage;

        return result;
    }

    public String getKey(){
        return name + "." + uuid;
    }

    public boolean isCompleted(){
        return "2".equalsIgnoreCase(type);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(DateTime completeTime) {
        this.completeTime = completeTime;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public String getBrowser() {
        return browser;
    }

    public String getPlatform() {
        return platform;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
