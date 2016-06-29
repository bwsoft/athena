package com.bwsoft.demo.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bwsoft.demo.storelocator.StoreLocatorException;

/**
 * This controller serves the Demo servlet. It has to be in a separate package from 
 * StoreLocatorController otherwise the resource can be leaked. 
 * 
 * This package is searched by Demo servlet for controller. It is configured in demo-servlet.xml.
 * 
 * @author yzhou
 *
 */
@Controller
@RequestMapping("/")
public class DemoController {

	@RequestMapping("/{ownerId}/feature")
	@ExceptionHandler(StoreLocatorException.class)
	public String hello(@PathVariable("ownerId") String owner, @RequestParam(value="msg", required=false) String msg, ModelMap map) {
		if( null != msg && "exception".equals(msg) ) {
			System.out.println("exception is thrown");
			throw new StoreLocatorException("An exception is triggered");			
		} else if( null != msg ) {
			map.addAttribute("msg", msg);
		} 

		map.addAttribute("ownerId", owner);
		map.addAttribute("helloStr", "Hello from Springframework MVC");
		
		return "featureDemo";
	}
	
//	@ExceptionHandler(StoreLocatorException.class)
//	public String handleStoreLocatorException(StoreLocatorException ex, HttpServletRequest req) {
//		return "ExceptionPage";
//	}
}
