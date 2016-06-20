package com.bwsoft.demo.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmployeeController {
	 
    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public ModelAndView showForm() {
    	// define a form whose modelAttribute is "employee" and whose underneath entity is Employee()
        return new ModelAndView("employeeHome", "employee", new Employee());
    }
 
    /**
     * An @ModelAttribute on a method argument indicates the argument will be retrieved from the model. 
     * If not present in the model, the argument will be instantiated first and then added to the model. 
     * 
     * The BindingResult argument needs to be positioned right after our form backing object – 
     * it’s one of the rare cases where the order of the method arguments matters. 
     * Now – an exception is no longer thrown; instead, errors will be registered on the BindingResult 
     * that is passed to the submit method. At this point, we can handle these errors 
     * in a variety of ways – for example, the operation can be canceled.
     * 
     * @param employee
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/addEmployee", method = RequestMethod.POST)
    public String submit(@Validated @ModelAttribute("employee")Employee employee, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "error";
        }
        
        model.addAttribute("name", employee.getName());
        model.addAttribute("contactNumber", employee.getContactNumber());
        model.addAttribute("id", employee.getId());
        return "employeeView";
    }

}
