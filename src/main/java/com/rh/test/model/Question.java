package com.rh.test.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@JsonIgnoreProperties("id")
@JsonPropertyOrder(value={"question","published_at","url","choices"})
public class Question {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	
	private String question;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date publishedAt;
	
	@Transient 
	private String url;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="question")
	private Set<Choice> choices;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@JsonProperty("published_at")
	public Date getPublishAt() {
		return publishedAt;
	}
	
	public void setPublishAt(Date publishAt) {
		this.publishedAt = publishAt;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<Choice> getChoices() {
		return choices;
	}

	public void setChoices(Set<Choice> choices) {
		this.choices = choices;
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", question=" + question + ", publishAt=" + publishedAt + ", url=" + url
				+ ", choices=" + choices + "]";
	}
}
