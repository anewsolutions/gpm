/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager.exception;

public class IssueException extends ManagerException {
  private static final long serialVersionUID = 1L;

  public IssueException() {
    super();
  }

  public IssueException(String message) {
    super(message);
  }

  public IssueException(String message, Throwable cause) {
    super(message, cause);
  }

  public IssueException(Throwable cause) {
    super(cause);
  }
}
