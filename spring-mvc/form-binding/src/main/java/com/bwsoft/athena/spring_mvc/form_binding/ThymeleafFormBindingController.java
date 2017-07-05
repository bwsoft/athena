package com.bwsoft.athena.spring_mvc.form_binding;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/thymeleaf")
public class ThymeleafFormBindingController
{
    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public String employeeForm(Model model)
    {
        // back up the form with Employee object. The form uses the key name, employee,
        // to refer to this object.
        model.addAttribute("employee", new Employee());
        return "employee.html";
    }

    @RequestMapping(value = "/employee", method = RequestMethod.POST)
    public String greetingSubmit(@ModelAttribute Employee employee, BindingResult result, ModelMap model)
    {
        // model contains the employee object under the key: employee.
        // It is also accessible via the ModelAttribute.
        return "result.html";
    }
}
