package com.miwashi.util;

import java.util.Date;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

public class StaticUtil {
	
	public static String toSince(Date date){
		DateTime now = DateTime.now();
        
        DateTime thisTime = new DateTime(date);
        long duration = now.getMillis() - thisTime.getMillis();
        if(duration < 0){
            return "not touched";
        }
        
        long days = Math.floorDiv(duration, (1000 * 3600 * 24 ));
        duration = duration - days * (1000 * 3600 * 24 );
        
        long hours = Math.floorDiv(duration, (1000 * 3600 ));
        duration = duration - hours * (1000 * 3600);
        
        long minutes = Math.floorDiv(duration, (1000 * 60 ));
        duration = duration - days * (1000 * 60);
        
        long seconds = Math.floorDiv(duration - days - hours - minutes, (1000));

        if(days>0){
            return days + " days ago!";
        }
        
        if(hours == 1){
        	return "1 hour" + ((minutes>1)?(" and " + minutes + " minutes "):"") + " ago.";
    	}

        if(hours>0){
        	return hours + " hours ago.";
        }

        if(minutes>0){
            return minutes + " minute" + ((minutes>1)?"s":"") + " ago!";
        }

        return seconds + " seconds ago.";
	}
	
	public static int toSinceDays(Date date){
		if(date == null){
            return Integer.MAX_VALUE;
        }

        DateTime now = DateTime.now();
        DateTime thisTime = new DateTime(date);
        long duration = now.getMillis() - thisTime.getMillis();
        if(duration < 0){
            return Integer.MAX_VALUE;
        }
        int days = new Long( Math.floorDiv(duration, (1000 * 3600 * 24 ))).intValue();
        return days;
	}
	
	public static int toSinceHours(Date date){
		if(date == null){
            return Integer.MAX_VALUE;
        }

        DateTime now = DateTime.now();
        DateTime thisTime = new DateTime(date);
        long duration = now.getMillis() - thisTime.getMillis();
        if(duration < 0){
            return Integer.MAX_VALUE;
        }
        int days = new Long( Math.floorDiv(duration, (1000 * 3600 * 24 ))).intValue();
        duration = duration - days * (1000 * 3600 * 24 );
        int hours = new Long(Math.floorDiv(duration, (1000 * 3600 ))).intValue();
        return hours;
	}
	
	public static int toSinceMinutes(Date date){
		if(date == null){
            return Integer.MAX_VALUE;
        }

        DateTime now = DateTime.now();
        DateTime thisTime = new DateTime(date);
        long duration = now.getMillis() - thisTime.getMillis();
        if(duration < 0){
            return Integer.MAX_VALUE;
        }
        int days = new Long( Math.floorDiv(duration, (1000 * 3600 * 24 ))).intValue();
        duration = duration - days * (1000 * 3600 * 24 );
        int hours = new Long(Math.floorDiv(duration, (1000 * 3600 ))).intValue();
        duration = duration - days * (1000 * 3600);
        int minutes = new Long(Math.floorDiv(duration, (1000 * 60 ))).intValue();
        return minutes;
	}
	
	public static int toSinceSeconds(Date date){
		if(date == null){
            return Integer.MAX_VALUE;
        }

        DateTime now = DateTime.now();
        DateTime thisTime = new DateTime(date);
        long duration = now.getMillis() - thisTime.getMillis();
        if(duration < 0){
            return Integer.MAX_VALUE;
        }
        int days = new Long( Math.floorDiv(duration, (1000 * 3600 * 24 ))).intValue();
        duration = duration - days * (1000 * 3600 * 24 );
        int hours = new Long(Math.floorDiv(duration, (1000 * 3600 ))).intValue();
        duration = duration - days * (1000 * 3600);
        int minutes = new Long(Math.floorDiv(duration, (1000 * 60 ))).intValue();
        duration = duration - days * (1000 * 60);
        int seconds = new Long(Math.floorDiv(duration, (1000))).intValue();
        return seconds;
	}
	
	public static String getJsonField(JSONObject obj, String field){
		String result = "";
		if(obj.has(field) && !(obj.isNull(field))){
			result = obj.getString(field);
		}
		return result;
	}
	
	public static int getJsonFieldAsInt(JSONObject obj, String field){
		int result = 0;
		if(!obj.has(field) || obj.isNull(field)){
			return result;
		}
		result = obj.getInt(field);
		return result;
	}
	
	public static JSONArray getJsonFieldAsArray(JSONObject obj, String field){
		JSONArray result = new JSONArray();
		if(obj.has(field)){
			result = obj.getJSONArray(field);
		}
		return result;
	}
}
