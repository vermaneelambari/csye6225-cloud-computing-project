package com.cloud.webapp.service;

import java.util.List;



import com.cloud.webapp.entity.Note;

public interface NoteService{
	
	public List<Note> findAll();

	public Note findById(String id);

	public Note save(Note note);

	public int delete(String id);

	public Note update(String id, Note note);

}
