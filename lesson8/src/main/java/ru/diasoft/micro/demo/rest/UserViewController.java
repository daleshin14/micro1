package ru.diasoft.micro.demo.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserViewController {
	
	@RequestMapping("/")
	public String getMainPage() {
		return "index";
	}
	
    @RequestMapping("/index")
    public String root() {
        return "index2";
    }

}
