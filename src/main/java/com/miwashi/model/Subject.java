package com.miwashi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Subject {
    private String name;
    private Map<String, String> requirements = new HashMap<String,String>();
    private String key;

    public Subject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<String> getRequirements() {
        return requirements.values();
    }

    public void add(Requirement requirement){
        if(!requirements.containsKey(requirement.getName())){
            requirements.put(requirement.getName(),requirement.getTestRequirement());
        }
    }

    public void setKey(String key) {
        this.key = key;
    }
}
