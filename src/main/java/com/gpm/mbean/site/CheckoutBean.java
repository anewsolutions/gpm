/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.codec.digest.DigestUtils;

import com.gpm.PayPointCallbackServlet;
import com.gpm.i18n.MessageProvider;
import com.gpm.manager.ConfigurationManager;
import com.gpm.manager.CustomerOrderManager;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.manager.exception.CustomerOrderException;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.Configuration;
import com.gpm.model.CustomerOrder;
import com.gpm.model.UserAccount;
import com.gpm.model.UserAddress;
import com.gpm.model.enums.Shipping;

@ManagedBean
@ViewScoped
public class CheckoutBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private UserAccount user;
  private CustomerOrder order;

  @PostConstruct
  public void init() {
    user = BeanUtils.fetchLoginBean().getUser();
    order = BeanUtils.fetchBasketBean().getOrder();
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
      address = getBillingAddress();
      user.setDeliveryAddress(address);
    }
    return address;
  }

  public boolean isAddressesSame() {
    return getBillingAddress().equals(getDeliveryAddress());
  }

  public void setAddressesSame(final boolean addressesSame) {
    if (addressesSame) {
      user.setDeliveryAddress(getBillingAddress());
    } else {
      UserAddress address = new UserAddress();
      address.setName(getBillingAddress().getName());
      address.setCompany(getBillingAddress().getCompany());
      address.setAddress1(getBillingAddress().getAddress1());
      address.setAddress2(getBillingAddress().getAddress2());
      address.setCity(getBillingAddress().getCity());
      address.setState(getBillingAddress().getState());
      address.setCountry(getBillingAddress().getCountry());
      address.setPostcode(getBillingAddress().getPostcode());
      user.setDeliveryAddress(address);
    }
  }

  public String[] getCountries() {
    return BeanUtils.generateCountriesList();
  }

  public String getShippingCategory() {
    return MessageProvider.getMessage("checkout2Shipping" + order.getShippingCategory());
  }

  public String getShippingCost() {
    return BeanUtils.formatPrice(order.getShippingPrice());
  }

  public String getGrandTotal() {
    return BeanUtils.formatPrice(order.getGrandTotal());
  }

  public String finishCheckout1() {
    order.setUser(user);
    if (order.getTotalOrderWeight() > 0) {
      // Calculate shipping category
      Shipping cat = BeanUtils.calculateShippingCategory(user.getDeliveryAddress().getCountry());
      order.setShippingCategory(cat);
      // Calculate shipping cost
      int cost = BeanUtils.calculateShippingCost(cat, order.getTotalOrderWeight());
      order.setShippingPrice(cost);
    } else {
      order.setShippingCategory(Shipping.NONE);
      order.setShippingPrice(0);
    }
    try {
      UserAccountManager.storeUserAccount(user);
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/checkout2.xhtml?faces-redirect=true";
  }

  public String finishCheckout2() {
    try {
      CustomerOrderManager.storeCustomerOrder(order);
    } catch (CustomerOrderException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/checkout3.xhtml?faces-redirect=true";
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

  public String getPaypointTransaction() {
    return order.getUuid().toString();
  }

  public String getPaypointAmount() {
    BigDecimal big = BigDecimal.valueOf(order.getGrandTotal()).scaleByPowerOfTen(-2);
    NumberFormat format = NumberFormat.getInstance(Locale.UK);
    return format.format(big.doubleValue());
  }

  public String getPaypointDigest() {
    String password = "";
    try {
      Configuration config = ConfigurationManager.findByKey("paypoint.password");
      password = config.getConfigValue();
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return DigestUtils.md5Hex(getPaypointTransaction() + getPaypointAmount() + password);
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
