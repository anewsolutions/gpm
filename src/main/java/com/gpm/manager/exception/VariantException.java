/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager.exception;

public class VariantException extends ManagerException {
  private static final long serialVersionUID = 1L;

  public VariantException() {
    super();
  }

  public VariantException(String message) {
    super(message);
  }

  public VariantException(String message, Throwable cause) {
    super(message, cause);
  }

  public VariantException(Throwable cause) {
    super(cause);
  }
}
