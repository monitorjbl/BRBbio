package com.thundermoose.bio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExcelUpload {
	
	public ModelAndView ui(){
		ModelAndView mv = new ModelAndView("excelUpload");
		return mv;
	}
}
