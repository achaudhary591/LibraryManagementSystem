package com.spark.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spark.lms.service.HomeService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	HomeService homeService;
	
	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public String homePage(Model model, HttpSession session) {
		model.addAttribute("topTiles", homeService.getTopTilesMap());
		
		// Add logged in user information to session
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			session.setAttribute("loggedInUserName", auth.getName());
			session.setAttribute("userRole", auth.getAuthorities().toString());
		}
		
		return "home";
	}	
	
}
