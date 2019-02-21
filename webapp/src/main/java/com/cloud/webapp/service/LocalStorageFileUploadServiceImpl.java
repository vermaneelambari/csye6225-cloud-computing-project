package com.cloud.webapp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("dev")
public class LocalStorageFileUploadServiceImpl implements FileUploadService {

	@Override
	public String uploadFile(MultipartFile multipartFile) throws Exception {
		String fileName = generateFileName(multipartFile);
		String uploadDir = "uploads/";
		String filePath = uploadDir + fileName;
		try {
			File file = new File(filePath);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(multipartFile.getBytes());
			fos.close();
		} catch (Exception e) {
			throw e;
		}

		return filePath;

	}

	@Override
	public void deleteFile(String fileName) throws Exception{
		String uploadDir = "uploads/";
		String filePath = uploadDir + fileName;
		File file = new File(filePath);
		
		try {
			file.delete();
		} catch (Exception e) {
			throw e;
		}

	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

}
