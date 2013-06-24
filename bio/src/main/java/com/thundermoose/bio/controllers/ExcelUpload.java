package com.thundermoose.bio.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.thundermoose.bio.dao.DataDao;
import com.thundermoose.bio.exceptions.DatabaseException;
import com.thundermoose.bio.model.Upload;
import com.thundermoose.bio.model.Upload.Item;

@Controller
public class ExcelUpload {

	@Autowired
	private DataDao	dao;

	@RequestMapping(value = "excelUpload")
	public ModelAndView ui() {
		ModelAndView mv = new ModelAndView("excelUpload");
		return mv;
	}

	@RequestMapping(value = "doLoad")
	public @ResponseBody
	Upload load(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String runName = request.getParameter("runName");
		if(runName == null || "".equals(runName)){
			throw new DatabaseException("Run name is required");
		}
		
		Item item = new Item();
		Upload upload = new Upload();
		upload.getFiles().add(item);
		
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Configure a repository (to ensure a secure temp location is used)
		ServletContext servletContext = request.getSession().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);

		// Create a new file upload handler
		ServletFileUpload up = new ServletFileUpload(factory);

		// Parse the request (sometimes theres fucking 3 of them, hell if i know why)
		List<FileItem> items = up.parseRequest(request);
		DiskFileItem it = null;
		for(FileItem fi : items){
			it = (DiskFileItem) fi;
		}
		item.setName(it.getName());
		item.setSize(it.getSize());
		it.write(it.getStoreLocation());

		dao.loadExcel(runName, new FileInputStream(it.getStoreLocation()));

		return upload;
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestError handleException(final Exception ex) throws IOException {
		RestError mv = new RestError();
		mv.setException(ex.getClass().getCanonicalName());
		mv.setMessage(ex.getLocalizedMessage());
		return mv;
	}

	private class RestError {
		private String	exception;
		private String	message;

		public String getException() {
			return exception;
		}

		public void setException(String exception) {
			this.exception = exception;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}
}
