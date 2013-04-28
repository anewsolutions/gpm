/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gpm.manager.ConfigurationManager;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.model.Configuration;

@ManagedBean
@ViewScoped
public class HomePageBean implements Serializable {
  private static final long serialVersionUID = 1L;

  public String getFacebookKey() {
    String key = "";
    try {
      Configuration config = ConfigurationManager.findByKey("facebook.key");
      key = config.getValue();
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return key;
  }
}
