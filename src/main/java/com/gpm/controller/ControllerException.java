/**
 * Copyright 2012-2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.controller;

/**
 * Generic exception type that gets thrown in the event of any kind of problem with JPA
 * entity persistence.
 * 
 * @author mbooth
 */
public class ControllerException extends Exception {
  private static final long serialVersionUID = 1L;

  public ControllerException() {
    super();
  }

  public ControllerException(String message) {
    super(message);
  }

  public ControllerException(String message, Throwable cause) {
    super(message, cause);
  }

  public ControllerException(Throwable cause) {
    super(cause);
  }
}
