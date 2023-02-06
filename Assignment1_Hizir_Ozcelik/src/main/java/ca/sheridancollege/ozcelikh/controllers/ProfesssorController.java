package ca.sheridancollege.ozcelikh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/professor")
public class ProfesssorController {
	
	@GetMapping()
	public String index() {
		return "professorIndex";
	}

}
