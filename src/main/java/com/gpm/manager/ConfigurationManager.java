/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.controller.ControllerFilter;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.model.Configuration;

public class ConfigurationManager {
  /**
   * Get the configuration item with the given UUID.
   * 
   * @param uuid
   *          the UUID of the configuration item requested
   * @return a configuration item or null if no configuration was found for the given UUID
   * @throws ConfigurationException
   *           if there was a problem fetching the configuration item
   */
  public static Configuration findByUuid(final String uuid) throws ConfigurationException {
    try {
      return ControllerFactory.getConfigurationController().get(UUID.fromString(uuid));
    } catch (IllegalArgumentException e) {
      throw new ConfigurationException(e);
    } catch (ControllerException e) {
      throw new ConfigurationException(e);
    }
  }

  /**
   * Get all configuration items.
   * 
   * @return the list of configuration items
   * @throws ConfigurationException
   *           if there was a problem fetching the list of configurations
   */
  public static List<Configuration> findAllConfigs() throws ConfigurationException {
    try {
      return ControllerFactory.getConfigurationController().getAll();
    } catch (ControllerException e) {
      throw new ConfigurationException(e);
    }
  }

  public static Properties findPropsByKey(final String key) throws ConfigurationException {
    try {
      Properties props = new Properties();
      List<ControllerFilter> filters = new ArrayList<ControllerFilter>();
      filters.add(new ControllerFilter("configKey", "like", key + "%"));
      List<Configuration> configs = ControllerFactory.getConfigurationController().getAll(filters);
      for (Configuration config : configs) {
        props.put(config.getConfigKey(), config.getConfigValue());
      }
      return props;
    } catch (ControllerException e) {
      throw new ConfigurationException(e);
    }
  }

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

  /**
   * Persist the given configuration to the data store.
   * 
   * @param config
   *          the configuration to be saved
   * @throws ConfigurationException
   *           if there was a problem saving the configuration
   */
  public static void save(final Configuration config) throws ConfigurationException {
    try {
      ControllerFactory.getConfigurationController().save(config);
    } catch (ControllerException e) {
      throw new ConfigurationException(e);
    }
  }

  /**
   * Delete the given configuration from the data store.
   * 
   * @param config
   *          the configuration to be deleted
   * @throws ConfigurationException
   *           if there was a problem deleting the configuration
   */
  public static void delete(final Configuration config) throws ConfigurationException {
    try {
      ControllerFactory.getConfigurationController().delete(config.getUuid());
    } catch (ControllerException e) {
      throw new ConfigurationException(e);
    }
  }
}
