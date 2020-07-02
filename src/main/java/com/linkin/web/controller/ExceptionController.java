package com.linkin.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice(basePackages = "com.linkin.web.controller")
public class ExceptionController extends BaseWebController{
	private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);
	
	@ExceptionHandler(value = { DataIntegrityViolationException.class })
	protected String handleConflict(Exception ex, Model model) {
		logger.info("Conflict: " + ex);
		model.addAttribute("msg", super.getMessage("error.msg.conflict"));
		return "admin/error/error";
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public String handleAccessDeniedException(Exception ex) {
		logger.info("Deny: " + ex);
		return "admin/error/deny";
	}
	
	@ExceptionHandler(MultipartException.class)
    public String handleMultipart(MultipartException ex, Model model) {
		logger.info("Max File: " + ex);
		model.addAttribute("msg", super.getMessage("error.msg.max.file.size"));
		return "admin/error/error";
    }

	@ExceptionHandler(value={NoHandlerFoundException.class})
    public String notFoundException(Exception ex, Model model) {
		return "client/common/error";
    }

	
	@ExceptionHandler({ Exception.class })
	public String handleInternalException(Exception ex, Model model) {
		logger.error("exception: " + ex);
		model.addAttribute("msg", super.getMessage("error.msg.general"));
		return "admin/error/error";
	}
}
