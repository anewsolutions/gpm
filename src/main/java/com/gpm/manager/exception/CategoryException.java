/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager.exception;

public class CategoryException extends ManagerException {
  private static final long serialVersionUID = 1L;

  public CategoryException() {
    super();
  }

  public CategoryException(String message) {
    super(message);
  }

  public CategoryException(String message, Throwable cause) {
    super(message, cause);
  }

  public CategoryException(Throwable cause) {
    super(cause);
  }
}
