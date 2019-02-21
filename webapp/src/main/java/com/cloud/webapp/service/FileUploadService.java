package com.cloud.webapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

	String uploadFile(MultipartFile multipartFile) throws Exception;

	void deleteFile(String fileName) throws Exception;
}
