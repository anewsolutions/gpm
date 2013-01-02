/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager.exception;

public class ManagerException extends Exception {
  private static final long serialVersionUID = 1L;

  public ManagerException() {
    super();
  }

  public ManagerException(String message) {
    super(message);
  }

  public ManagerException(String message, Throwable cause) {
    super(message, cause);
  }

  public ManagerException(Throwable cause) {
    super(cause);
  }
}
