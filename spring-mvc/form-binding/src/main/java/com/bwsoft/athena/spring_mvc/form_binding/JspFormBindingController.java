package com.bwsoft.athena.spring_mvc.form_binding;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/jsp")
public class JspFormBindingController
{
    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public ModelAndView showForm()
    {
        Employee employee = new Employee();
        employee.setName("default name");
        return new ModelAndView(
                "employee",  // view name. Prefix and suffix are defined in the view resolver
                "employee",  // modelAttribute name. It is also the value to be used in the modelAttribute of the form.
                employee     // corresponding object for the module.
                );
    }

    @RequestMapping(value = "/employee", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, ModelMap model)
    {
        if (result.hasErrors())
        {
            return "error";
        }

        return "result";
    }
}
