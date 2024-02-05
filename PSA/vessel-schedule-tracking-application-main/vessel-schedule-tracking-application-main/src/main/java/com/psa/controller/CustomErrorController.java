package com.psa.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
 
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
 
@Controller
public class CustomErrorController  implements ErrorController {
 
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        String errorPage = "error"; // default error page
         
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
         
        if (status != null) {
            String statusCode = status.toString();
            model.addAttribute("first", statusCode.substring(0, 1));
            model.addAttribute("second", statusCode.substring(1, 2));
            model.addAttribute("third", statusCode.substring(2, 3));
        }  
        return errorPage;
    }
     
    @Override
    public String getErrorPath() {
        return "/error";
    }
}

