package com.project.Mesa.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	@GetMapping("/login")
    public String login() {
        return "/paginas/login";
    }
}
