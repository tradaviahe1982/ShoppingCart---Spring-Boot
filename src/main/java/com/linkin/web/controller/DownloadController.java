package com.linkin.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkin.utils.FileStore;

@Controller
public class DownloadController {
	@RequestMapping(value = "/user/file/{file:.+}")
	public void download(@PathVariable(value = "file") String fileName, HttpServletResponse response) {
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName + "\""));

			File file = new File(fileName);
			Files.copy(file.toPath(), response.getOutputStream());
		} catch (IOException e) {
		}
	}

	@RequestMapping(value = "/image/{file:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] download(@PathVariable(value = "file") String fileName) {
		String filePath = FileStore.UPLOAD_FOLDER + File.separator + fileName;
		File file = new File(filePath);
		try {
			return Files.readAllBytes(file.toPath());
		} catch (IOException e) {
		}
		return new byte[0];
	}
}
