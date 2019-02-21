package com.cloud.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.webapp.dao.AttachmentDao;
import com.cloud.webapp.entity.Attachment;

@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {
	
	private AttachmentDao attachmentDao;
	
	@Autowired
	public AttachmentServiceImpl(AttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
	}

	@Override
	public List<Attachment> findall() {
		return attachmentDao.findall();
	}

	@Override
	public Attachment findById(String id) {
		
		return attachmentDao.findById(id);
	}

	@Override
	public Attachment save(Attachment attachment) {
		
		return attachmentDao.save(attachment);
	}

	@Override
	public int delete(String id) {
		
		return attachmentDao.delete(id);
	}

	@Override
	public Attachment update(String id, Attachment attachment) {
		
		return attachmentDao.update(id, attachment);
	}

}
