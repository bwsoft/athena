package com.bwsoft.demo.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The @RequestMapping annotation allows you to map HTTP requests to specific controller methods. 
 * The two methods in this controller are both mapped to /greeting. By default @RequestMapping maps 
 * all HTTP operations, such as GET, POST, and so forth. But in this case the greetingForm() method 
 * is specifically mapped to GET using @RequestMapping(method=GET), while greetingSubmit() is mapped 
 * to POST with @RequestMapping(method=POST). This mapping allows the controller to differentiate 
 * the requests to the /greeting endpoint. 
 * 
 * The greetingForm() method uses a Model object to expose a new Greeting to the view template and 
 * will be used to capture the information from the form.
 * 
 * The @ModelAttribute indicates the value is from the HttpRequest
 * 
 * @author yzhou
 *
 */
@Controller
public class ThymeleafController {

    @RequestMapping(value="/greeting", method=RequestMethod.GET)
    public String greetingForm(Model model) {
    	// adds a model "greeting".
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @RequestMapping(value="/greeting", method=RequestMethod.POST)
    public String greetingSubmit(@ModelAttribute("greeting") Greeting greeting, Model model) {
    	// set greeting to the current greeting received.
        model.addAttribute("greeting", greeting);
        model.addAttribute("id", greeting.getId());
        return "result";
    }
}
