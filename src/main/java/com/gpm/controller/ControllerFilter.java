/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.controller;

public class ControllerFilter {
  public final String field;
  public final String operator;
  public final Object value;

  public ControllerFilter(final String field, final String operator, final Object value) {
    this.field = field;
    this.operator = operator;
    this.value = value;
  }
}
