/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.controller;

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
