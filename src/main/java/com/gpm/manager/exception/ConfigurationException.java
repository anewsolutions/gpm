/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager.exception;

public class ConfigurationException extends ManagerException {
  private static final long serialVersionUID = 1L;

  public ConfigurationException() {
    super();
  }

  public ConfigurationException(String message) {
    super(message);
  }

  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConfigurationException(Throwable cause) {
    super(cause);
  }
}
