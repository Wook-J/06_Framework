package edu.kh.project.email.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.email.model.service.EmailService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("email")
@RequiredArgsConstructor
public class EmailController {

	private final EmailService service;
	
	@ResponseBody
	@PostMapping("signup")
	private int signup(@RequestBody String email) {
		String authKey = service.sendEmail("signup", email);
		
		if(authKey != null) return 1;
		
		return 0;
	}
	
	@ResponseBody
	@PostMapping("checkAuthKey")	// email, authKey 들어있는 map
	private int checkAuthKey(@RequestBody Map<String, String> map) {
		return service.checkAuthKey(map);
	}
}
