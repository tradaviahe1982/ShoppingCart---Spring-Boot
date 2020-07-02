package com.linkin.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonWebController extends BaseWebController {

	@RequestMapping(value = "/")
	private String home(HttpServletRequest request) {
		return "redirect:/dang-nhap";
	}
	
	@GetMapping("/admin/home-page")
	public String homePage() {
		return "admin/home-page";
	}
	
	@GetMapping("/admin/xml-converter")
	public String cutOutput() {
		return "admin/xmlConverter/xml-converter";
	}

	@RequestMapping(value = {"/access-deny"})
	private String deny(HttpServletRequest request) {
		return getViewName(request, "admin/error/deny");
	}
	
}
