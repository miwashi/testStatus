package com.miwashi.model;

import javax.persistence.*;

@Entity(name = "BROWSERS")
public class Browser {

    public static final String DEFAULT_BROWSER = "firefox";
    @Column(name = "BROWSER_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

    @Column(unique = true)
    private String name = "name";


    public Browser(){
        super();
    }

    public Browser(String name){
        this.name = name;
    }

    public String toString(){
		return id + " - " + name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
