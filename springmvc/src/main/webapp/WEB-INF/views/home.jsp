<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Spring MVC Demonstration</title>
    </head>
    <body>
        <h1>Spring MVC Demonstration</h1>
        <p>
        This project is created by spring-mvc-archetype. It is annotation based spring MVC. 
        The WebApplicationContext is replaced by AnnotationConfigWebApplicationContext. 
        The configurations for beans are defined in MvcConfiguration which is annotated as @Configuration.
        See web.xml on setting it up. 
        </p>
        
        <p>
        The example, <a href="./employee"> ./employee </a>, demonstrates Spring MVC form binding.
        It also shows how the exception is handled when the form contains a negative employee ID.
        </p>
    </body>
</html>
