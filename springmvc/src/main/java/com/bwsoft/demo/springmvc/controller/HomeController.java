package com.bwsoft.demo.springmvc.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@RequestMapping(value="/")
	public ModelAndView test(HttpServletResponse response) throws IOException{
		return new ModelAndView("home");
	}
	
	@RequestMapping(value="/myview")
	public String myview(ModelMap model) {
		// add an attribute to model. The attribute can be retrieved by the model using ${attr-name}
		model.addAttribute("message", "a message to myview");
		
		// return the view name. It is mapped to myview.jsp. The suffix is defined in the MvcConfiguration.java
		return "myview";
	}
}
