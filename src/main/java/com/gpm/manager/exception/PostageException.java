/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager.exception;

public class PostageException extends ManagerException {
  private static final long serialVersionUID = 1L;

  public PostageException() {
    super();
  }

  public PostageException(String message) {
    super(message);
  }

  public PostageException(String message, Throwable cause) {
    super(message, cause);
  }

  public PostageException(Throwable cause) {
    super(cause);
  }
}
