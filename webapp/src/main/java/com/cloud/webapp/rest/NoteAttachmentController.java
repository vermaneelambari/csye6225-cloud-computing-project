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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.webapp.entity.Attachment;
import com.cloud.webapp.entity.Note;
import com.cloud.webapp.entity.User;
import com.cloud.webapp.service.FileUploadService;
import com.cloud.webapp.service.AttachmentService;
import com.cloud.webapp.service.NoteService;
import com.cloud.webapp.service.UserService;

@RestController
@RequestMapping("/note")
public class NoteAttachmentController {

	private FileUploadService fileUploadService;
	private AttachmentService attachmentService;
	private NoteService noteService;
	private UserService userService;

	@Autowired
	public NoteAttachmentController(FileUploadService amazonS3ClientService, NoteService theNoteService,
			UserService theUerService, AttachmentService attachmentService) {
		this.fileUploadService = amazonS3ClientService;
		this.attachmentService = attachmentService;
		this.noteService = theNoteService;
		this.userService = theUerService;
	}
	
	@GetMapping("/{id}/attachments")
	public ResponseEntity<?> getAllAttachments(@PathVariable String id){
		Map<String, String> map = new HashMap<>();
		SecurityContext context = SecurityContextHolder.getContext();
		String email = context.getAuthentication().getName();
		User user = userService.findByEmail(email);
		
		try {
			Note note = noteService.findById(id);
			if (note == null) {
				map.put("Failure", "Note not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

			if (note.getUser() != user) {
				map.put("Error", "You are not authenticated");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}
			
			List<Attachment> attachments = attachmentService.findall().stream().filter(attachment -> attachment.getNote() == note)
					.collect(Collectors.toList());
			return new ResponseEntity<>(attachments, HttpStatus.OK);
		} catch (Exception e) {
			map.put("Error", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/{id}/attachments")
	public ResponseEntity<?> uploadFile(@PathVariable String id, @Valid @RequestPart(value = "file") MultipartFile file) {
		Map<String, String> map = new HashMap<>();

		// If file is not attached
//		if (result.hasErrors()) {
//			StringJoiner sj = new StringJoiner(", ");
//			for (ObjectError objError : result.getAllErrors()) {
//				sj.add(objError.getDefaultMessage());
//			}
//			map.put("Error", sj.toString());
//			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
//		}

		// To check if note exists and correct user is logged in
		SecurityContext context = SecurityContextHolder.getContext();
		String email = context.getAuthentication().getName();
		User user = userService.findByEmail(email);
		Note note = null;
		try {
			note = noteService.findById(id);
			if (note == null) {
				map.put("Failure", "Note not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

			if (note.getUser() != user) {
				map.put("Error", "You are not authenticated");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}

			String url = this.fileUploadService.uploadFile(file);
			Attachment attachment = new Attachment();
			attachment.setId(null);
			attachment.setNote(note);
			attachment.setUrl(url);
			Attachment savedAttachment = attachmentService.save(attachment);
			map.put("Success", "File uploaded successfully");
			return new ResponseEntity<>(savedAttachment, HttpStatus.CREATED);
		} catch (Exception e) {
			map.put("Error", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
	
	}
	
	@DeleteMapping("/{id}/attachments/{attachmentId}")
	public ResponseEntity<?> deleteAttachment(@PathVariable String id, @PathVariable String attachmentId){
		Map<String, String> map = new HashMap<>();
		SecurityContext context = SecurityContextHolder.getContext();
		String email = context.getAuthentication().getName();
		User user = userService.findByEmail(email);
		
		Note note = null;
		int status = 0;
		try {
			note = noteService.findById(id);

			if (note == null) {
				map.put("Failure", "Note not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

			if (note.getUser() != user) {
				map.put("Error", "You are not authenticated");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}
			Attachment attachment = attachmentService.findById(attachmentId);
			
			if(attachment == null) {
				map.put("Failure", "Attachment not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}
			
			if(attachment.getNote() != note) {
				map.put("Error", "You are not authenticated");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}
			
			String fileName = attachment.getUrl().substring(attachment.getUrl().lastIndexOf("/")+1);
			
			this.fileUploadService.deleteFile(fileName);
			
			status = attachmentService.delete(attachmentId);
			
			
		} catch (Exception ex) {
			map.put("Error", ex.getMessage());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
		
		if (status != 1) {
			map.put("Error", "Could not delete note");
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(map, HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/{id}/attachments/{attachmentId}")
	public ResponseEntity<?> updateAttachment(@PathVariable String id,  @PathVariable String attachmentId, @Valid @RequestPart(value = "file") MultipartFile file){
		Map<String, String> map = new HashMap<>();
		SecurityContext context = SecurityContextHolder.getContext();
		String email = context.getAuthentication().getName();
		User user = userService.findByEmail(email);
		
		Note note = null;
		
		try {
			note = noteService.findById(id);

			if (note == null) {
				map.put("Failure", "Note not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

			if (note.getUser() != user) {
				map.put("Error", "You are not authenticated");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}
			Attachment attachment = attachmentService.findById(attachmentId);
			
			if(attachment == null) {
				map.put("Failure", "Attachment not found");
				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}
			
			if(attachment.getNote() != note) {
				map.put("Error", "You are not authenticated");
				return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
			}
			
			String fileName = attachment.getUrl().substring(attachment.getUrl().lastIndexOf("/")+1);
			
			this.fileUploadService.deleteFile(fileName);
			
			String url = this.fileUploadService.uploadFile(file);
			Attachment updatedAttachment = new Attachment();
			updatedAttachment.setId(null);
			updatedAttachment.setNote(note);
			updatedAttachment.setUrl(url);
			Attachment savedAttachment = attachmentService.update(attachmentId, updatedAttachment);
			return new ResponseEntity<>(map, HttpStatus.NO_CONTENT);			
			
		} catch (Exception ex) {
			map.put("Error", ex.getMessage());
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

	}

}
