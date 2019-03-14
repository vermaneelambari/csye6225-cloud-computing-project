package com.cloud.webapp.dao;

import java.util.List;

import com.cloud.webapp.entity.Note;

public interface NoteDao {

	public List<Note> findAll();

	public Note findById(String id);

	public Note save(Note note);

	public int delete(String id);

	//public Note update(String id, Note note);
	public Note update(String id, Note note);
}
