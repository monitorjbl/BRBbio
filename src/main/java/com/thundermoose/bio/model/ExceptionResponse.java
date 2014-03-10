package com.thundermoose.bio.model;

/**
 * Created by tayjones on 3/10/14.
 */
public class ExceptionResponse {
  private String message;
  private String type;
  private String stacktrace;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStacktrace() {
    return stacktrace;
  }

  public void setStacktrace(String stacktrace) {
    this.stacktrace = stacktrace;
  }
}
