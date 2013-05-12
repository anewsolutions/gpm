/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.gpm.PayPointCallbackServlet;
import com.gpm.manager.ConfigurationManager;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.Configuration;
import com.gpm.model.UserAccount;
import com.gpm.model.UserAddress;

@ManagedBean
@ViewScoped
public class CheckoutBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private UserAccount user;

  @PostConstruct
  public void init() {
    user = BeanUtils.fetchLoginBean().getUser();
  }

  public UserAddress getBillingAddress() {
    UserAddress address = user.getBillingAddress();
    if (address == null) {
      address = new UserAddress();
      address.setName(user.getName());
      address.setCountry("UNITED KINGDOM");
      user.setBillingAddress(address);
    }
    return address;
  }

  public UserAddress getDeliveryAddress() {
    UserAddress address = user.getDeliveryAddress();
    if (address == null) {
      address = new UserAddress();
      address.setName(user.getName());
      address.setCountry("UNITED KINGDOM");
      user.setDeliveryAddress(address);
    }
    return address;
  }

  public String[] getCountries() {
    return BeanUtils.generateCountriesList();
  }

  public String finishCheckout1() {
    try {
      UserAccountManager.storeUserAccount(user);
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/checkout2.xhtml?faces-redirect=true";
  }

  public String finishCheckout2() {
    return "";
  }

  public String getPaypointMerchant() {
    String merchant = "";
    try {
      Configuration config = ConfigurationManager.findByKey("paypoint.merchant");
      merchant = config.getConfigValue();
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
