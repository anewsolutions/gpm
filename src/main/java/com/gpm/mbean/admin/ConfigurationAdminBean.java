/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.gpm.manager.ConfigurationManager;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.model.Configuration;

@ManagedBean
@ViewScoped
public class ConfigurationAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private boolean editing;
  private Configuration selected;

  /**
   * Bean initialisation.
   */
  @PostConstruct
  public void init() {
    try {
      // Load the selected item, if an ID is passed as a query parameter
      FacesContext fc = FacesContext.getCurrentInstance();
      String uuid = fc.getExternalContext().getRequestParameterMap().get("uuid");
      if (uuid != null && !uuid.isEmpty()) {
        selected = ConfigurationManager.findByUuid(uuid);
        editing = true;
      } else {
        selected = new Configuration();
      }
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * JSF method to determine whether we are editing a config or adding a new config.
   * 
   * @return true if editing an existing config, false if adding a new config
   */
  public boolean isEditing() {
    return editing;
  }

  /**
   * JSF method to provide access to the currently selected configuration.
   * 
   * @return the selected configuration
   */
  public Configuration getSelected() {
    return selected;
  }

  /**
   * JSF method to provide access to all products.
   * 
   * @return a list of products
   */
  public List<Configuration> getAll() {
    List<Configuration> all = null;
    try {
      all = ConfigurationManager.findAllConfigs();
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }

  /**
   * JSF method to save the selected configuration.
   */
  public String save() {
    try {
      ConfigurationManager.save(selected);
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/config_list?faces-redirect=true";
  }

  /**
   * JSF method to delete the selected configuration.
   */
  public String delete() {
    try {
      ConfigurationManager.delete(selected);
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/config_list?faces-redirect=true";
  }
}
