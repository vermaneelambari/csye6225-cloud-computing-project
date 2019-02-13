package com.cloud.webapp.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cloud.webapp.entity.Note;

@Repository
public class NoteDaoImpl implements NoteDao {

	private EntityManager entityManager;

	@Autowired
	public NoteDaoImpl(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}

	@Override
	public List<Note> findAll() {

		Session currentSession = entityManager.unwrap(Session.class);

		Query<Note> query = currentSession.createQuery("from Note", Note.class);

		List<Note> notes = query.getResultList();

		return notes;
	}

	@Override
	public Note findById(String id) {
		UUID uid = UUID.fromString(id);
		Session currentSession = entityManager.unwrap(Session.class);

		Note note = currentSession.get(Note.class, uid);

		return note;
	}

	@Override
	public Note save(Note note) {
		// note.setId(id);
		Session currentSession = entityManager.unwrap(Session.class);
		note.setCreated_on(new Date().toString());
		note.setLast_updated_on(new Date().toString());
		currentSession.saveOrUpdate(note);
		return note;
	}

	@Override
	public int delete(String id) {
		UUID uid = UUID.fromString(id);
		Session currentSession = entityManager.unwrap(Session.class);

		Query<Note> query = currentSession.createQuery("delete from Note where id=:noteid");

		query.setParameter("noteid", uid);

		int result = query.executeUpdate();
		
		return result;

	}

	@Override
	public Note update(String id, Note note) {
		UUID uid = UUID.fromString(id);
		Session currentSession = entityManager.unwrap(Session.class);

		Note noteexisting = currentSession.get(Note.class, uid);
		if(noteexisting != null) {
				noteexisting.setContent(note.getContent());
				noteexisting.setTitle(note.getTitle());
				noteexisting.setLast_updated_on(new Date().toString());
				entityManager.merge(noteexisting);
				return noteexisting;
		}
		return null;
	}

}
