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

  private String configKey;
  private String configValue;

  public Configuration() {
    super();
  }

  @Column(unique = true, nullable = false)
  public String getConfigKey() {
    return configKey;
  }

  public void setConfigKey(final String configKey) {
    this.configKey = configKey;
  }

  @Column(nullable = false)
  public String getConfigValue() {
    return configValue;
  }

  public void setConfigValue(final String configValue) {
    this.configValue = configValue;
  }
}
