package com.cloud.webapp.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cloud.webapp.entity.Attachment;

@Repository
public class AttachmentDaoImpl implements AttachmentDao {

	private EntityManager entityManager;

	@Autowired
	public AttachmentDaoImpl(EntityManager theEntityManager) {
		//entityManager = theEntityManager;
		entityManager = theEntityManager;
	}

	@Override
	public List<Attachment> findall() {

		Session currentSession = entityManager.unwrap(Session.class);

		Query<Attachment> query = currentSession.createQuery("from Attachment", Attachment.class);

		List<Attachment> attachments = query.getResultList();

		return attachments;
	}

	@Override
	public Attachment findById(String id) {

		UUID uid = UUID.fromString(id);

		Session currentSession = entityManager.unwrap(Session.class);

		Attachment attachment = currentSession.get(Attachment.class, uid);

		return attachment;
	}

	@Override
	public Attachment save(Attachment attachment) {

		Session currentSession = entityManager.unwrap(Session.class);

		currentSession.saveOrUpdate(attachment);

		return attachment;
	}

	@Override
	public int delete(String id) {

		UUID uid = UUID.fromString(id);
		Session currentSession = entityManager.unwrap(Session.class);

		Query<Attachment> query = currentSession.createQuery("delete from Attachment where id=:attachmentid");

		query.setParameter("attachmentid", uid);

		int result = query.executeUpdate();

		return result;
	}

	@Override
	public Attachment update(String id, Attachment attachment) {
		UUID uid = UUID.fromString(id);
		Session currentSession = entityManager.unwrap(Session.class);

		Attachment attachmentexisting = currentSession.get(Attachment.class, uid);
		if (attachmentexisting != null) {
			attachmentexisting.setUrl(attachment.getUrl());
			entityManager.merge(attachmentexisting);
			return attachmentexisting;
		}
		return null;
	}

}
