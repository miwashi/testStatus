package com.miwashi.model.transients.jenkins;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {
	private boolean building;
	private String description;
	private String displayName;
	private long duration;
	private long estimatedDuration;
	private String fullDisplayName;
	private String id;
	private boolean keepLog;
	private long number;
	private long queueId;
	private String result;
	private Date timestamp;
	private String url;
	private String builtOn;
	
	public boolean isBuilding() {
		return building;
	}
	public void setBuilding(boolean building) {
		this.building = building;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public long getEstimatedDuration() {
		return estimatedDuration;
	}
	public void setEstimatedDuration(long estimatedDuration) {
		this.estimatedDuration = estimatedDuration;
	}
	public String getFullDisplayName() {
		return fullDisplayName;
	}
	public void setFullDisplayName(String fullDisplayName) {
		this.fullDisplayName = fullDisplayName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isKeepLog() {
		return keepLog;
	}
	public void setKeepLog(boolean keepLog) {
		this.keepLog = keepLog;
	}
	public long getNumber() {
		return number;
	}
	public void setNumber(long number) {
		this.number = number;
	}
	public long getQueueId() {
		return queueId;
	}
	public void setQueueId(long queueId) {
		this.queueId = queueId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBuiltOn() {
		return builtOn;
	}
	public void setBuiltOn(String builtOn) {
		this.builtOn = builtOn;
	}
	
	public String toString(){
		return new Gson().toJson(this);
	}
}
