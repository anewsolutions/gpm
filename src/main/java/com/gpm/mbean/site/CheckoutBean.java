/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.gpm.PayPointCallbackServlet;
import com.gpm.manager.ConfigurationManager;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.model.Configuration;

@ManagedBean
@SessionScoped
public class CheckoutBean implements Serializable {
  private static final long serialVersionUID = 1L;

  public String getPaypointMerchant() {
    String merchant = "";
    try {
      Configuration config = ConfigurationManager.findByKey("paypoint.merchant");
      merchant = config.getValue();
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return merchant;
  }

  public String getPaypointCallback() {
    ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
    String server = ctx.getRequestScheme() + "://" + ctx.getRequestServerName() + ":" + ctx.getRequestServerPort();
    return server + PayPointCallbackServlet.PAYPOINT_PATH;
  }

  public String getPaypointBackCallback() {
    ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
    String server = ctx.getRequestScheme() + "://" + ctx.getRequestServerName() + ":" + ctx.getRequestServerPort();
    return server + "/basket.xhtml";
  }
}
