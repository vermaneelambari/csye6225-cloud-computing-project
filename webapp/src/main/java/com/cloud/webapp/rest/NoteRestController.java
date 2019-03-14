package com.cloud.webapp.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.webapp.entity.Attachment;
import com.cloud.webapp.entity.Note;
import com.cloud.webapp.entity.User;
import com.cloud.webapp.service.FileUploadService;
import com.cloud.webapp.service.NoteService;
import com.cloud.webapp.service.UserService;

@RestController
public class NoteRestController {

	private NoteService noteService;
	private UserService userService;
	private FileUploadService fileUploadService;

	@Autowired
	public NoteRestController(NoteService theNoteService, UserService theUerService, FileUploadService theFileUploadService) {
		noteService = theNoteService;
		userService = theUerService;
		fileUploadService = theFileUploadService;
	}

	@GetMapping("/note")
	public List<Note> findAll() {
		SecurityContext context = SecurityContextHolder.getContext();
		String email = context.getAuthentication().getName();
		User user = userService.findByEmail(email);
		List<Note> notes = noteService.findAll().stream().filter(note -> note.getUser() == user)
				.collect(Collectors.toList());
		return notes;
	}

	@GetMapping(path = "/note/{id}")
	public ResponseEntity<?> findOne(@PathVariable String id) {
		Map<String, String> map = new HashMap<>();
		SecurityContext context = SecurityContextHolder.getContext();
		String email = context.getAuthentication().getName();
		User user = userService.findByEmail(email);
		Note note = null;
		try {
			note = noteService.findById(id);
			if (note == null) {
				map.put("Failure", "Key not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

			if (note.getUser() != user) {
				map.put("Error", "You are not authenticated");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			map.put("Error", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		map.put(id, note.toString());
		return new ResponseEntity<>(note, HttpStatus.OK);
	}

	@PostMapping("/note")
	public ResponseEntity<?> createNote(@Valid @RequestBody Note note, BindingResult result) {
		
		if(result.hasErrors()) {
			StringJoiner sj = new StringJoiner(", ");
			for(ObjectError objError : result.getAllErrors()) {
				sj.add(objError.getDefaultMessage()); 
			}
			Map<String, String> map = new HashMap<>();
			map.put("Error", sj.toString());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
		SecurityContext context = SecurityContextHolder.getContext();
		String email = context.getAuthentication().getName();
		User user = userService.findByEmail(email);
		note.setId(null);
		note.setUser(user);
		Map<String, String> map = new HashMap<>();
		try {
			Note savedNote = noteService.save(note);
			map.put("Success", "Note created successfully with id: " + note.getId());
			return new ResponseEntity<>(savedNote, HttpStatus.CREATED);
		} catch (Exception e) {
			map.put("Error", "bad Request");
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

	}

	@DeleteMapping("/note/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable String id) {
		Map<String, String> map = new HashMap<String, String>();
		SecurityContext context = SecurityContextHolder.getContext();
		String email = context.getAuthentication().getName();
		User user = userService.findByEmail(email);
		Note note = null;
		int status = 0;
		try {
			note = noteService.findById(id);

			if (note == null) {
				map.put("Failure", "Key not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

			if (note.getUser() != user) {
				map.put("Error", "You are not authenticated");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}
			for(Attachment attachment: note.getAttachments()) {
				String fileName = attachment.getUrl().substring(attachment.getUrl().lastIndexOf("/")+1);
				this.fileUploadService.deleteFile(fileName);
			}
			status = noteService.delete(id);
		} catch (Exception ex) {
			map.put("Error", ex.getMessage());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		if (status != 1) {
			map.put("Error", "Could not delete note");
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		map.put("Success", "Record deleted successfully");
		return new ResponseEntity<>(map, HttpStatus.NO_CONTENT);

	}

	@PutMapping("/note/{id}")
	public ResponseEntity<?> updateNote(@Valid @RequestBody Note note, BindingResult result, @PathVariable String id) {
		Map<String, String> map = new HashMap<String, String>();
		if(result.hasErrors()) {
			StringJoiner sj = new StringJoiner(", ");
			for(ObjectError objError : result.getAllErrors()) {
				sj.add(objError.getDefaultMessage()); 
			}
			map.put("Error", sj.toString());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
		
		SecurityContext context = SecurityContextHolder.getContext();
		String email = context.getAuthentication().getName();
		User user = userService.findByEmail(email);
		Note updatedNote = null;
		try {

			updatedNote = noteService.findById(id);

			if (updatedNote == null) {
				map.put("Failure", "Key not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

			if (updatedNote.getUser() != user) {
				map.put("Error", "You are not authenticated");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}

			updatedNote = noteService.update(id, note);
			
		} catch (Exception ex) {
			map.put("Error", ex.getMessage());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		map.put("Success", "Record updated successfully");
		return new ResponseEntity<>(map, HttpStatus.NO_CONTENT);
	}
}
