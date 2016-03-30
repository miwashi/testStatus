package net.miwashi.teststatus.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "PLATFORMS")
public class Platform {

    public static final String DEFAULT_PLATFORM = "linux";
    @Column(name = "PLATFORM_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

    @Column(unique = true)
    private String name = "name";


    public Platform(){
        super();
    }

    public Platform(String name){
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
