/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.util.ArrayList;
import java.util.List;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.controller.ControllerFilter;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.model.Configuration;

public class ConfigurationManager {
  public static Configuration findByKey(final String key) throws ConfigurationException {
    try {
      List<ControllerFilter> filters = new ArrayList<ControllerFilter>();
      filters.add(new ControllerFilter("configKey", "=", key));
      List<Configuration> configs = ControllerFactory.getConfigurationController().getAll(filters);
      // Configuration keys are unique, so should be safe to return first result only
      if (!configs.isEmpty()) {
        return configs.get(0);
      } else {
        return null;
      }
    } catch (ControllerException e) {
      throw new ConfigurationException(e);
    }
  }

  public static void storeConfig(final String key, final String value) throws ConfigurationException {
    // Store configuration
    Configuration config = findByKey(key);
    if (config == null) {
      config = new Configuration();
    }
    config.setConfigKey(key);
    config.setConfigValue(value);
    try {
      ControllerFactory.getConfigurationController().save(config);
    } catch (ControllerException e) {
      throw new ConfigurationException(e);
    }
  }
}
