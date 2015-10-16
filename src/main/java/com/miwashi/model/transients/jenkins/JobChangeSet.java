package com.miwashi.model.transients.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobChangeSet {
	private String comment;
	private Author author;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public class Author{
		private String fullName;

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}
		
		
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String toString(){
		return new Gson().toJson(this);
	}
}
