package com.cloud.webapp.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.timgroup.statsd.StatsDClient;

@RestController
public class NoteRestController {

	private NoteService noteService;
	private UserService userService;
	private FileUploadService fileUploadService;
	private StatsDClient statsDClient;
	private static final Logger logger = LoggerFactory.getLogger(NoteRestController.class);

	@Autowired
	public NoteRestController(NoteService theNoteService, UserService theUerService, FileUploadService theFileUploadService, StatsDClient theStatsDClient) {
		noteService = theNoteService;
		userService = theUerService;
		fileUploadService = theFileUploadService;
		statsDClient = theStatsDClient;
	}

	@GetMapping("/note")
	public List<Note> findAll() {
		statsDClient.incrementCounter("endpoint.note.api.get");
		logger.info("endpoint.note.api.get hit successfully");
		SecurityContext context = SecurityContextHolder.getContext();
		String email = context.getAuthentication().getName();
		User user = userService.findByEmail(email);
		List<Note> notes = noteService.findAll().stream().filter(note -> note.getUser() == user)
				.collect(Collectors.toList());
		return notes;
	}

	@GetMapping(path = "/note/{id}")
	public ResponseEntity<?> findOne(@PathVariable String id) {
		statsDClient.incrementCounter("endpoint.note.api.get");
		logger.info("endpoint.note.api.get hit successfully");
		Map<String, String> map = new HashMap<>();
		SecurityContext context = SecurityContextHolder.getContext();
		String email = context.getAuthentication().getName();
		User user = userService.findByEmail(email);
		Note note = null;
		try {
			note = noteService.findById(id);
			if (note == null) {
				map.put("Failure", "Key not found");
				logger.warn("Note with id "+id+" not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

			if (note.getUser() != user) {
				map.put("Error", "You are not authenticated");
				logger.warn("User is not authenticted");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			map.put("Error", e.getMessage());
			logger.error(e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		map.put(id, note.toString());
		logger.info("Note with id "+id+" found successfully");
		return new ResponseEntity<>(note, HttpStatus.OK);
	}

	@PostMapping("/note")
	public ResponseEntity<?> createNote(@Valid @RequestBody Note note, BindingResult result) {
		statsDClient.incrementCounter("endpoint.note.api.post");
		logger.info("endpoint.note.api.post hit successfully");
		if(result.hasErrors()) {
			StringJoiner sj = new StringJoiner(", ");
			for(ObjectError objError : result.getAllErrors()) {
				sj.add(objError.getDefaultMessage()); 
			}
			Map<String, String> map = new HashMap<>();
			map.put("Error", sj.toString());
			logger.warn(sj.toString());
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
			logger.info("Note with created successfully");
			return new ResponseEntity<>(savedNote, HttpStatus.CREATED);
		} catch (Exception e) {
			map.put("Error", "bad Request");
			logger.error(e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

	}

	@DeleteMapping("/note/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable String id) {
		statsDClient.incrementCounter("endpoint.note.api.delete");
		logger.info("endpoint.note.api.delete hit successfully");
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
				logger.warn("Note with id "+id+" not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

			if (note.getUser() != user) {
				map.put("Error", "You are not authenticated");
				logger.warn("User is not authenticted");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}
			for(Attachment attachment: note.getAttachments()) {
				String fileName = attachment.getUrl().substring(attachment.getUrl().lastIndexOf("/")+1);
				this.fileUploadService.deleteFile(fileName);
			}
			status = noteService.delete(id);
		} catch (Exception ex) {
			map.put("Error", ex.getMessage());
			logger.error(ex.getMessage());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		if (status != 1) {
			map.put("Error", "Could not delete note");
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		map.put("Success", "Record deleted successfully");
		logger.info("Note with id "+id+" deleted successfully");
		return new ResponseEntity<>(map, HttpStatus.NO_CONTENT);

	}

	@PutMapping("/note/{id}")
	public ResponseEntity<?> updateNote(@Valid @RequestBody Note note, BindingResult result, @PathVariable String id) {
		statsDClient.incrementCounter("endpoint.note.api.put");
		logger.info("endpoint.note.api.put hit successfully");
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
				logger.warn("Note with id "+id+" not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

			if (updatedNote.getUser() != user) {
				map.put("Error", "You are not authenticated");
				logger.warn("User is not authenticted");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}

			updatedNote = noteService.update(id, note);
			
		} catch (Exception ex) {
			map.put("Error", ex.getMessage());
			logger.error(ex.getMessage());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		map.put("Success", "Record updated successfully");
		logger.info("Note with id "+id+" updated successfully");
		return new ResponseEntity<>(map, HttpStatus.NO_CONTENT);
	}
}
