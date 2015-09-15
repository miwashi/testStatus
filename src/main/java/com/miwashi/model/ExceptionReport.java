package com.miwashi.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by miwa01 on 15-07-16.
 */
public class ExceptionReport {

	private String uuid;
    private String name;
    private String message;
    
    public ExceptionReport(String msg){

        JsonObject json = (new JsonParser()).parse(msg).getAsJsonObject();
        if(!json.has("type")){
            throw new IllegalStateException("Missing type field");
        }
        if(!json.has("name")){
            throw new IllegalStateException("Missing name field");
        }
        uuid = json.has("uuid")?json.get("uuid").getAsString():"";
        name = json.has("name")?json.get("name").getAsString():"";
        message = json.has("exception")?json.get("exception").getAsString():"";
    }

    public String toString(){
        String result = "";

        result += name;
        result += " " + uuid;
        result += " " + message;
        
        return result;
    }

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setName(String name) {
		this.name = name;
	}
	
    public String getKey(){
        return name + "." + uuid;
    }


    public String getName() {
        return name;
    }
    
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static boolean isException(String msg){
        JsonObject json = (new JsonParser()).parse(msg).getAsJsonObject();
        if(!json.has("type")){
            return false;
        }
        String type = json.has("type")?json.get("type").getAsString():"";
        return "3".equalsIgnoreCase(type);
    }
}
