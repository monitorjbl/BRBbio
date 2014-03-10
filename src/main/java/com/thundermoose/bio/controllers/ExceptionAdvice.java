package com.thundermoose.bio.controllers;

import com.thundermoose.bio.model.ExceptionResponse;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by tayjones on 3/10/14.
 */
@ControllerAdvice
public class ExceptionAdvice {
  private static final Logger logger = Logger.getLogger(ExceptionAdvice.class);

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ExceptionResponse handleAllException(Exception ex) {
    logger.error(ex.getMessage(), ex);

    ExceptionResponse r = new ExceptionResponse();
    r.setMessage(ex.getMessage());
    r.setType(ex.getClass().getCanonicalName());
    r.setStacktrace(ExceptionUtils.getFullStackTrace(ex));
    return r;
  }
}
