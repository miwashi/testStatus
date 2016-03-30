package net.miwashi.teststatus.model;

import org.joda.time.DateTime;

import net.miwashi.receiver.Notification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by miwa01 on 15-07-24.
 */
public class Stats {

    private static final int DAYS_IN_WEEK = 7;
    private static final int DAYS_IN_MONTH = 32; //Add one as dates don't start on 0.
    private static final int HOURS_IN_DAY = 24;
    private static final int MINUTES_IN_HOUR = 60;

    private Stat total = new Stat();
    private Stat[] minutely = new Stat[MINUTES_IN_HOUR];
    private Stat[] hourly = new Stat[HOURS_IN_DAY];
    private Stat[] daily = new Stat[DAYS_IN_MONTH];
    private Map<String, Stat> teamly = new HashMap<String, Stat>();
    private Map<String, Stat> jobly = new HashMap<String, Stat>();
    
    private int numberOfRequirements = 0;
    private int numberOfTestedRequirements = 0;
    private int numberOfUnstableRequirements = 0;
    private int numberOfVerifiedRequirements = 0;
    private int numberOfFailedRequirements = 0;
    
    private int numberOfUniqueJobs = 0;
    private int numberOfJobs = 0;
    private int numberOfTeams = 0;
    private int numberOfJenkises = 0;
    private int numberOfBrowsers = 0;
    private int numberOfPlatforms = 0;
    
    private Set<String> jenkises = new HashSet<String>();
    private Set<String> teams = new HashSet<String>();
    private Set<String> browsers = new HashSet<String>();
    private Set<String> platforms = new HashSet<String>();
    private Set<String> jobs = new HashSet<String>();

    public class Stat{
        private String caption = "";
        private long starts =0;
        private long complete = 0;
        private long fails = 0;
        private long skipped = 0;
        private long succeeded = 0;
        private long jobs = 0;

        public Stat(){
            super();
        }

        public Stat(long starts, long complete, long succeeded, long fails, long skipped){
            super();
            this.starts = starts;
            this.complete = complete;
            this.fails = fails;
            this.skipped = skipped;
            this.succeeded = succeeded;
        }

        public long getStarts() {
            return starts;
        }
        
        public long getStartsWithoutSkipped() {
            return starts - skipped;
        }

        public void setStarts(long starts) {
            this.starts = starts;
        }

        public long getComplete() {
            return complete;
        }
        
        public long getCompleteWithoutSkipped() {
            return complete - skipped;
        }

        public void setComplete(long complete) {
            this.complete = complete;
        }

        public long getFails() {
            return fails;
        }
        
        public long getFailsWithoutSkipped() {
            return fails - skipped;
        }

        public void setFails(long fails) {
            this.fails = fails;
        }

        
        
        public long getSkipped() {
			return skipped;
		}

		public void setSkipped(long skipped) {
			this.skipped = skipped;
		}

		public long getSucceeded() {
			return succeeded;
		}

		public void setSucceeded(long succeeded) {
			this.succeeded = succeeded;
		}

		public String getFailShare(){
            String result = "0%";
            long numberOfTestsRun = complete - skipped;

            if(numberOfTestsRun>0){
                result = "" + Math.round((fails*100) / numberOfTestsRun) + "%";
            }
            return result;
        }

        public String getCompleteWithMissing(){
            String result = "" + complete +"%";
            if(complete>0){
                result = complete + " (" + (starts - complete) + ")";
            }
            return result;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }
    }

    public Stats(){
        super();
        for(int i=0; i < DAYS_IN_MONTH; i++){
            daily[i]= new Stat();
        }

        for(int i=0; i < HOURS_IN_DAY; i++){
            hourly[i]= new Stat();
        }

        for(int i=0; i < MINUTES_IN_HOUR; i++){
            minutely[i]= new Stat();
        }
    }

    public void add(Notification report){
        DateTime now = DateTime.now();
        int hourOfDay = now.getHourOfDay();
        int dayOfMonth = now.getDayOfMonth();
        int minuteOfHour = now.getMinuteOfHour();
        int nextMinuteOfHour = now.plusMinutes(1).getMinuteOfHour();


        if (report.isCompleted()) {
            total.complete++;
            hourly[hourOfDay].complete++;
            daily[dayOfMonth].complete++;
            minutely[minuteOfHour].complete++;
            minutely[nextMinuteOfHour].complete=0;
            
            if (report.isFailed()) {
                total.fails++;
                hourly[hourOfDay].fails++;
                daily[dayOfMonth].fails++;
                minutely[minuteOfHour].fails++;
                minutely[nextMinuteOfHour].fails=0;
            }
            
            if (report.isSkipped()) {
                total.skipped++;
                hourly[hourOfDay].skipped++;
                daily[dayOfMonth].skipped++;
                minutely[minuteOfHour].skipped++;
                minutely[nextMinuteOfHour].skipped=0;
            }
            
            if (report.isSucceeded()) {
                total.succeeded++;
                hourly[hourOfDay].succeeded++;
                daily[dayOfMonth].succeeded++;
                minutely[minuteOfHour].succeeded++;
                minutely[nextMinuteOfHour].succeeded=0;
            }
        }else{
            total.starts++;
            hourly[hourOfDay].starts++;
            daily[dayOfMonth].starts++;
            minutely[minuteOfHour].starts++;
            minutely[nextMinuteOfHour].starts=0;
        }
    }

    public int getNumberOfRequirements() {
		return numberOfRequirements;
	}

	public void setNumberOfRequirements(int numberOfRequirements) {
		this.numberOfRequirements = numberOfRequirements;
	}

	public int getNumberOfTestedRequirements() {
		return numberOfTestedRequirements;
	}

	public void setNumberOfTestedRequirements(int numberOfTestedRequirements) {
		this.numberOfTestedRequirements = numberOfTestedRequirements;
	}

	public int getNumberOfUnstableRequirements() {
		return numberOfUnstableRequirements;
	}

	public void setNumberOfUnstableRequirements(int numberOfUnstableRequirements) {
		this.numberOfUnstableRequirements = numberOfUnstableRequirements;
	}

	public int getNumberOfVerifiedRequirements() {
		return numberOfVerifiedRequirements;
	}

	public void setNumberOfVerifiedRequirements(int numberOfVerifiedRequirements) {
		this.numberOfVerifiedRequirements = numberOfVerifiedRequirements;
	}

	public int getNumberOfFailedRequirements() {
		return numberOfFailedRequirements;
	}

	public void setNumberOfFailedRequirements(int numberOfFailedRequirements) {
		this.numberOfFailedRequirements = numberOfFailedRequirements;
	}

	
	
	public int getNumberOfUniqueJobs() {
		return numberOfUniqueJobs;
	}

	public void setNumberOfUniqueJobs(int numberOfUniqueJobs) {
		this.numberOfUniqueJobs = numberOfUniqueJobs;
	}

	public int getNumberOfJobs() {
		return numberOfJobs;
	}

	public void setNumberOfJobs(int numberOfJobs) {
		this.numberOfJobs = numberOfJobs;
	}

	public int getNumberOfTeams() {
		return numberOfTeams;
	}

	public void setNumberOfTeams(int numberOfTeams) {
		this.numberOfTeams = numberOfTeams;
	}

	public int getNumberOfJenkises() {
		return numberOfJenkises;
	}

	public void setNumberOfJenkises(int numberOfJenkises) {
		this.numberOfJenkises = numberOfJenkises;
	}

	public int getNumberOfBrowsers() {
		return numberOfBrowsers;
	}

	public void setNumberOfBrowsers(int numberOfBrowsers) {
		this.numberOfBrowsers = numberOfBrowsers;
	}

	public int getNumberOfPlatforms() {
		return numberOfPlatforms;
	}

	public void setNumberOfPlatforms(int numberOfPlatforms) {
		this.numberOfPlatforms = numberOfPlatforms;
	}

	public Set<String> getJenkises() {
		return jenkises;
	}

	public void setJenkises(Set<String> jenkises) {
		this.jenkises = jenkises;
	}

	public Set<String> getTeams() {
		return teams;
	}

	public void setTeams(Set<String> teams) {
		this.teams = teams;
	}

	public Set<String> getBrowsers() {
		return browsers;
	}

	public void setBrowsers(Set<String> browsers) {
		this.browsers = browsers;
	}

	public Set<String> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(Set<String> platforms) {
		this.platforms = platforms;
	}

	public Set<String> getJobs() {
		return jobs;
	}

	public void setJobs(Set<String> jobs) {
		this.jobs = jobs;
	}

	public Stat getTotal(){
        return  total;
    }

    public Stat getLast24Hours(){
        long total = 0;
        long complete = 0;
        long fails = 0;
        long skipped = 0;
        long succeeded = 0;

        for(int i=0; i < hourly.length; i++){
            total+= hourly[i].getStarts();
            complete+= hourly[i].getComplete();
            fails+= hourly[i].getFails();
            skipped+= hourly[i].getSkipped();
            succeeded+= hourly[i].getSucceeded();
        }
        return  new Stat(total, complete, succeeded, fails, skipped);
    }

    public Stat getLastHour(){
        long total = 0;
        long complete = 0;
        long fails = 0;
        long skipped = 0;
        long succeeded = 0;

        for(int i=0; i < minutely.length; i++){
            total+= minutely[i].getStarts();
            complete+= minutely[i].getComplete();
            succeeded+= minutely[i].getSucceeded();
            fails+= minutely[i].getFails();
            skipped+= minutely[i].getSkipped();
        }
        return  new Stat(total, complete, succeeded, fails, skipped);
    }

    public Stat getLastWeek(){
        long total = 0;
        long complete = 0;
        long fails = 0;
        long skipped = 0;
        long succeeded = 0;

        DateTime now = DateTime.now();
        total= daily[now.getDayOfMonth()].getStarts();
        complete= daily[now.getDayOfMonth()].getComplete();
        fails= daily[now.getDayOfMonth()].getFails();
        skipped= daily[now.getDayOfMonth()].getSkipped();
        for(int i=1; i < 7; i++){
            total+= daily[now.minusDays(i).getDayOfMonth()].getStarts();
            complete+= daily[now.minusDays(i).getDayOfMonth()].getComplete();
            fails+= daily[now.minusDays(i).getDayOfMonth()].getFails();
            skipped+= daily[now.minusDays(i).getDayOfMonth()].getSkipped();
            succeeded+= daily[now.minusDays(i).getDayOfMonth()].getSucceeded();
        }
        return  new Stat(total, complete, succeeded, fails, skipped);
    }

    public Stat getToday(){
        long total = 0;
        long complete = 0;
        long fails = 0;
        long skipped = 0;
        long succeeded = 0;

        DateTime now = DateTime.now();
        total= hourly[now.getHourOfDay()].getStarts();
        complete= hourly[now.getHourOfDay()].getComplete();
        fails= hourly[now.getHourOfDay()].getFails();
        skipped = hourly[now.getHourOfDay()].getSkipped();
        int thisDay = now.getDayOfMonth();
        int indX = 1;
        while(thisDay == now.minusHours(indX).getDayOfMonth()){
            total+= hourly[now.minusHours(indX).getHourOfDay()].getStarts();
            complete+= hourly[now.minusHours(indX).getHourOfDay()].getComplete();
            fails+= hourly[now.minusHours(indX).getHourOfDay()].getFails();
            skipped+= hourly[now.minusHours(indX).getHourOfDay()].getSkipped();
            succeeded+= hourly[now.minusHours(indX).getHourOfDay()].getSucceeded();
            indX++;
        }
        return  new Stat(total, complete, succeeded, fails, skipped);
    }

    public Collection<Stat> getHourly(){
        Collection<Stat> result = new ArrayList<Stat>();
        DateTime now = DateTime.now();
        for(int i =0; i < 24; i++){
            DateTime aHour = now.minusHours(i);
            hourly[aHour.getHourOfDay()].setCaption(aHour.toString("HH:00") + "-" +aHour.plusHours(1).toString("HH:00"));
            result.add(hourly[aHour.getHourOfDay()]);
        }
        return result;
    }

    public Collection<Stat> getDaily(){
        Collection<Stat> result = new ArrayList<Stat>();
        DateTime now = DateTime.now();
        for(int i =0; i < 7; i++){
            DateTime aDay = now.minusDays(i);
            daily[aDay.getDayOfMonth()].setCaption(aDay.toString("yyyy-MM-dd"));
            result.add(daily[aDay.getDayOfMonth()]);
        }
        return result;
    }
    
    public Map<String, Stat> getTeamly(){
    	return teamly;
    }
    
    public Map<String, Stat> getJobly(){
    	return teamly;
    }

    public void resetHour(){
        DateTime now = DateTime.now();
        int hourOfDay = now.getHourOfDay();
        hourly[hourOfDay].starts=0;
        hourly[hourOfDay].complete=0;
        hourly[hourOfDay].fails=0;
        hourly[hourOfDay].skipped=0;
        hourly[hourOfDay].succeeded=0;
    }

    public void resetDay(){
        DateTime now = DateTime.now();
        int dayOfMonth = now.getDayOfMonth();
        daily[dayOfMonth].starts=0;
        daily[dayOfMonth].complete=0;
        daily[dayOfMonth].fails=0;
        daily[dayOfMonth].skipped=0;
        daily[dayOfMonth].succeeded=0;
    }
}
