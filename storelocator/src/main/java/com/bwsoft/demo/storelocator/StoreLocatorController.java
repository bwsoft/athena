package com.bwsoft.demo.storelocator;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This controller serves the StoreLocator servlet. It has to be in a separate package from 
 * DemoController otherwise the resource can be leaked. 
 * 
 * This package is searched by StoreLocator servlet for controller. It is configured in storelocator-servlet.xml.
 * 
 * @author yzhou
 *
 */
@Controller
@RequestMapping("/")
public class StoreLocatorController implements BeanFactoryAware {

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory arg0) throws BeansException {
		beanFactory = arg0;
	}

	@RequestMapping("/mystoreSearch") 
	@ExceptionHandler(StoreLocatorException.class)
	public @ResponseBody String getMyStore(@RequestParam(value="lat", required=true) float lat, @RequestParam(value="lng", required=true) float lng, @RequestParam(value="radius", required=true) float radius) {
		StoreLocatorQuery query = (StoreLocatorQuery) beanFactory.getBean("storeLocatorQuery");
		try {
			return query.queryStores(lat, lng, radius);
		} catch (Exception e) {
			throw new StoreLocatorException(e.getMessage());
		}		
	}
	
	@RequestMapping("/mystore")
	public String searchStore(ModelMap model) {
		StoreLocatorQuery query = (StoreLocatorQuery) beanFactory.getBean("storeLocatorQuery");
		String apiKey = null;
		try{
			apiKey = query.getGoogleAPIKey();
		} catch( Exception e ) {
			throw new StoreLocatorException(e.getMessage());
		}
		model.addAttribute("apiKey", apiKey);
		return "mystore";
	}
}
