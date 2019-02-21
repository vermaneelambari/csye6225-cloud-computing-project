package com.cloud.webapp.dao;

import java.util.List;

import com.cloud.webapp.entity.Attachment;

public interface AttachmentDao {

	public List<Attachment> findall();
	
	public Attachment findById(String id);
	
	public Attachment save(Attachment attachment);
	
	public int delete(String id);
	
	public Attachment update(String id, Attachment attachment);
}
