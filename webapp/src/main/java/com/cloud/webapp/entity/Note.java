package com.cloud.webapp.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="note")
public class Note implements Serializable {
	
	@Id
	@Type(type = "uuid-char")
	@GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

	@Column(name="content")
	@NotNull(message="Content is required")
	@NotBlank(message="Content cannot be blank")
	private String content;
	
	@Column(name="title")
	@NotNull(message="Title is required")
	@NotBlank(message="Title cannot be blank")
	private String title;
	
	@Column(name="created_on")
	private String created_on;
	
	@Column(name="last_updated")
	private String last_updated_on;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private User user;
	
	@OneToMany(mappedBy="note", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Attachment> attachments;
	
	
	public Note(UUID id, String content, String title,String created_on,String last_updated_on, User user, List<Attachment> attachments) {
		super();
		this.id = id;
		this.content = content;
		this.title = title;
		this.created_on = created_on;
		this.last_updated_on = last_updated_on;
		this.user = user;
		this.attachments = attachments;
	}
	
	public Note() {
		
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	public String getLast_updated_on() {
		return last_updated_on;
	}

	public void setLast_updated_on(String last_updated_on) {
		this.last_updated_on = last_updated_on;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

}