/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager.exception;

public class EmailException extends ManagerException {
  private static final long serialVersionUID = 1L;

  public EmailException() {
    super();
  }

  public EmailException(String message) {
    super(message);
  }

  public EmailException(String message, Throwable cause) {
    super(message, cause);
  }

  public EmailException(Throwable cause) {
    super(cause);
  }
}
