package com.miwashi.model;


import com.miwashi.config.ApplicationConfig;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(name = "RESULTS")
public class Result implements Comparable<Result>{

    @Column(name = "RESULT_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

    @Column(name = "REQUIREMENT_ID")
	private long requirementId;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "PLATFORM_ID")
    private Platform platform;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "BROWSER_ID")
    private Browser browser;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date timeStamp;

    @Column(name = "STATUS")
    private boolean status;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date completionTime;


    public Result(){
        super();
    }

    public Result(String status){
        this.status = "success".equalsIgnoreCase(status);
        this.timeStamp = new Date();
    }


    public Result(boolean status){
        this.status = status;
        this.timeStamp = new Date();
    }

    public Result(long requirementId, Boolean status) {
        this.requirementId = requirementId;
        this.status = status;
    }

    public String toString(){
		return id + " - " + status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

    public long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(long requirementId) {
        this.requirementId = requirementId;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus(){
        return status;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Browser getBrowser() {
        return browser;
    }

    public void setBrowser(Browser browser) {
        this.browser = browser;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Date getStartTime() {
        return startTime;
    }

    public String getStartTimeStr() {
        if(startTime==null){
            return "n/a";
        }
        return ApplicationConfig.TIME_STAMP.format(startTime);
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(DateTime startTime) {
        if(startTime==null){
            return;
        }
        this.startTime = startTime.toDate();
    }

    public Date getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Date completionTime) {
        this.completionTime = completionTime;
    }

    public void setCompletionTime(DateTime completionTime) {
        if(completionTime==null){
            return;
        }
        this.completionTime = completionTime.toDate();
    }

    public long getDuration(){
        if((completionTime == null)||(startTime==null)){
            return 0;
        }
        return completionTime.getTime() - startTime.getTime();
    }

    @Override
    public int compareTo(Result comparedResult) {
        if((comparedResult==null)||(comparedResult.getStartTime()==null)||(this.getStartTime()==null)){
            return 0;
        }
        return this.getStartTime().compareTo(comparedResult.getStartTime());
    }
}
