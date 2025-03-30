package com.springboot.dev_spring_boot_demo.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Get the error status code from the request
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // Check if the error code is 404 (Not Found)
            if (statusCode == 404) {
                return "redirect:/admin/accessDenied";
            }

            if (statusCode == 500) {
                return "redirect:/admin/accessDenied";
            }
        }

        // Return the default error page for other errors
        return "error";
    }
}
