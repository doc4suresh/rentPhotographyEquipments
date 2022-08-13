package com.kodak.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kodak.constants.NotiType;
import com.kodak.utility.NotificationModel;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String springLogin(Model model) {

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", noti);

		return "login";
	}

	@GetMapping("/")
	public String home(Model model) {

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", noti);

		return "index";
	}

	@GetMapping("/accessDenied")
	public String accessDenied(Model model) {

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", noti);

		return "accessDenied";
	}
}
