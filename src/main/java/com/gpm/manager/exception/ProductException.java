/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager.exception;

public class ProductException extends ManagerException {
  private static final long serialVersionUID = 1L;

  public ProductException() {
    super();
  }

  public ProductException(String message) {
    super(message);
  }

  public ProductException(String message, Throwable cause) {
    super(message, cause);
  }

  public ProductException(Throwable cause) {
    super(cause);
  }
}
