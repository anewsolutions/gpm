/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager.exception;

public class UserAccountException extends ManagerException {
  private static final long serialVersionUID = 1L;

  public UserAccountException() {
    super();
  }

  public UserAccountException(String message) {
    super(message);
  }

  public UserAccountException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserAccountException(Throwable cause) {
    super(cause);
  }
}
