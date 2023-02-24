package ca.sheridancollege.ozcelikh.controllers;

/*
 * Author: Ozcelik Hizir
 * Date: 2023-02-15
 * Description: This is a controller class for home page
 */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

	@GetMapping(value = { "/", "" })
	public String index(Model model) {

		return "index";
	}

}
