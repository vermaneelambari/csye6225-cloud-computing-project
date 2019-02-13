package com.cloud.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.webapp.dao.NoteDao;
import com.cloud.webapp.entity.Note;



@Service
@Transactional
public class NoteServiceImpl implements NoteService {
	
	private NoteDao noteDao;
	
	@Autowired
	public NoteServiceImpl(NoteDao noteDao) {
		this.noteDao = noteDao;
	}

	@Override
	public List<Note> findAll() {
		
		return noteDao.findAll();
		
	}

	@Override
	public Note findById(String id) {
		
		return noteDao.findById(id);
		
	}

	@Override
	public Note save(Note note) {
		noteDao.save(note);
		return note;
	}

	@Override
	public int delete(String id) {
		
		return noteDao.delete(id);

	}

	@Override
	public Note update(String id, Note note) {
		return noteDao.update(id, note);
	}

}
