package com.miwashi.model;

/**
 * Created by miwa01 on 15-07-09.
 */
public class Setting implements Comparable<Setting>{
    private String key = "";
    private String value = "";

    public Setting(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(Setting setting) {
        return key.compareTo(setting.getKey());
    }
}
