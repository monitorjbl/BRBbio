package com.thundermoose.bio.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thundermoose.bio.dao.DataDao;
import com.thundermoose.bio.model.Plate;
import com.thundermoose.bio.model.RawData;

@Controller
public class ViewData {

	@Autowired
	private DataDao dao;
	
	@RequestMapping(value = "viewData")
	public ModelAndView ui() {
		ModelAndView mv = new ModelAndView("viewData");
		mv.addObject("plates", getPlates());
		return mv;
	}
	
	@RequestMapping(value="getPlates")
	public @ResponseBody List<Plate> getPlates(){
		return dao.getPlates();
	}
	
	@RequestMapping(value="getRawDataForPlate")
	public @ResponseBody List<Plate> getRawDataForPlate(@RequestParam long plateId){
		return dao.getDataByPlate(plateId);
	}
	
	@RequestMapping(value="getRawDataForPlateHorizontal")
	public @ResponseBody List<Plate> getRawDataForPlateHorizontal(@RequestParam long plateId){
		return dao.getDataByPlate(plateId);
	}	
}
