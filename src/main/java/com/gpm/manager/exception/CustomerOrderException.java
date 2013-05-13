/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager.exception;

public class CustomerOrderException extends ManagerException {
  private static final long serialVersionUID = 1L;

  public CustomerOrderException() {
    super();
  }

  public CustomerOrderException(String message) {
    super(message);
  }

  public CustomerOrderException(String message, Throwable cause) {
    super(message, cause);
  }

  public CustomerOrderException(Throwable cause) {
    super(cause);
  }
}
