package com.miwashi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "CHECKIN_COMMENT")
public class CheckinComment {
	
	@Column(name = "COMMENT_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

    //@Column(name = "JOB_ID")
	//private long jobId;
    
	private String author;
	private String comment;
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
