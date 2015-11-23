package com.miwashi.jsonapi;

import java.util.Date;

import org.joda.time.DateTime;

import com.miwashi.config.ApplicationConfig;

public class SimpleTimeStamp {
	
	private Date when;
	private long minutesSince;
	private String since;
	private String timeStamp;
	
	public SimpleTimeStamp(Date when){
		this.when = when;
		if(when==null){
			timeStamp = "never";
			minutesSince = 0;
			since = "never";
			return;
		}
		timeStamp = when==null?"":ApplicationConfig.TIME_STAMP.format(when);
		minutesSince = prepareMinutesSince();
		since = prepareSince();
	}
	
	public String getSince() {
		return since;
	}
	
	public Date getTime() {
		return when;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public long getMinutesSince() {
		return minutesSince;
	}

	public long prepareMinutesSince() {
		if(when == null){
            return 0;
        }

        DateTime now = DateTime.now();
        DateTime thisTime = new DateTime(when.getTime());
        long duration = now.getMillis() - thisTime.getMillis();
        if(duration < 0){
            return 0;
        }
        long minutes = Math.floorDiv(duration, (1000 * 60 ));
        return minutes;
	}
	
	public String prepareSince() {
        if(when == null){
            return "not touched";
        }

        DateTime now = DateTime.now();
        DateTime thisTime = new DateTime(when.getTime());
        long duration = now.getMillis() - thisTime.getMillis();
        if(duration < 0){
            return "not touched";
        }
        long days = Math.floorDiv(duration, (1000 * 3600 * 24 ));
        duration = duration - days * (1000 * 3600 * 24 );
        long hours = Math.floorDiv(duration, (1000 * 3600 ));
        duration = duration - days * (1000 * 3600);
        long minutes = Math.floorDiv(duration, (1000 * 60 ));
        duration = duration - days * (1000 * 60);
        long seconds = Math.floorDiv(duration, (1000));

        if(days>0){
            return days + " days ago";
        }

        if(hours>0){
            return hours + " hour, " + ((hours>1)?"s":"") + " minutes ago";
        }

        if(minutes>0){
            return minutes + " minute" + ((minutes>1)?"s":"") + " ago";
        }

        return seconds + " seconds ago";
    }

	public int compareTo(SimpleTimeStamp when) {
		if(this.when==null || when==null || when.getTime()==null){
			return 0;
		}
		return Long.compare(this.when.getTime(), when.getTime().getTime());
	}
}
