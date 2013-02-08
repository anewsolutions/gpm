/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager.exception;

public class ImageException extends ManagerException {
  private static final long serialVersionUID = 1L;

  public ImageException() {
    super();
  }

  public ImageException(String message) {
    super(message);
  }

  public ImageException(String message, Throwable cause) {
    super(message, cause);
  }

  public ImageException(Throwable cause) {
    super(cause);
  }
}
