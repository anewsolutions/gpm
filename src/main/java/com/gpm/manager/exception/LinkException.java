/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager.exception;

public class LinkException extends ManagerException {
  private static final long serialVersionUID = 1L;

  public LinkException() {
    super();
  }

  public LinkException(String message) {
    super(message);
  }

  public LinkException(String message, Throwable cause) {
    super(message, cause);
  }

  public LinkException(Throwable cause) {
    super(cause);
  }
}
