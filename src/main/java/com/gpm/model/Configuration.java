/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A JPA entity for storing a configuration key/value pair in the database.
 * 
 * @author mbooth
 */
@Entity
public class Configuration extends Base {
  private static final long serialVersionUID = 1L;

  private String key;
  private String value;

  public Configuration() {
    super();
  }

  @Column(name = "configKey", unique = true, nullable = false)
  public String getKey() {
    return key;
  }

  public void setKey(final String key) {
    this.key = key;
  }

  @Column(name = "configValue", nullable = false)
  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }
}
