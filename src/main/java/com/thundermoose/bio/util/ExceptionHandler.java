package com.thundermoose.bio.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.thundermoose.bio.exceptions.UserNotFoundException;

public class ExceptionHandler implements HandlerExceptionResolver, Ordered {
	private static final Logger logger = Logger.getLogger(ExceptionHandler.class);
	
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception e) {
		resp.setStatus(500);
		
		if(!(e instanceof UserNotFoundException)){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error(sw.toString());
		}
		
		ModelAndView mv = new ModelAndView("exceptionHandler");
		mv.addObject("exception", e);
		return mv;
	}

}
