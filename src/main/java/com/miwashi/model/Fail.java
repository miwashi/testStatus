package com.miwashi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="FAILS")
public class Fail {
	
	@Column(name = "FAIL_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
	@Column(name = "RESULT_ID")
	private long resultId;
	
	@Column(name = "message", length=1024)
	private String message;

	public Fail(){
		super();
	}
	
	public Fail(ExceptionReport exceptionReport) {
		super();
		this.message = exceptionReport.getMessage();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getResultId() {
		return resultId;
	}

	public void setResultId(long resultId) {
		this.resultId = resultId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
