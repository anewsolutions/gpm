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
import com.gpm.manager.IssueManager;
import com.gpm.manager.PostageManager;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.manager.exception.CustomerOrderException;
import com.gpm.manager.exception.IssueException;
import com.gpm.manager.exception.PostageException;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.mbean.BeanUtils;
import com.gpm.model.Configuration;
import com.gpm.model.CustomerOrder;
import com.gpm.model.CustomerOrderItem;
import com.gpm.model.Issue;
import com.gpm.model.UserAccount;
import com.gpm.model.UserAddress;
import com.gpm.model.enums.OrderType;
import com.gpm.model.enums.Shipping;

@ManagedBean
@ViewScoped
public class CheckoutBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private UserAccount user;
  private CustomerOrder order;

  @PostConstruct
  public void init() {
    try {
      user = UserAccountManager.findCurrentlyLoggedIn();
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    order = BeanUtils.fetchBasketBean().getOrder();
  }

  public UserAddress getBillingAddress() {
    return user.getBillingAddress();
  }

  public UserAddress getDeliveryAddress() {
    return user.getDeliveryAddressFacade();
  }

  public String getEmailAddress() {
    return user.getEmail();
  }

  public boolean isAddressesSame() {
    return user.isDeliverySameAsBilling();
  }

  public void setAddressesSame(final boolean addressesSame) {
    user.setDeliverySameAsBilling(addressesSame);
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
    try {
      Issue currentIssue = IssueManager.findCurrentIssue();
      if (order.getTotalOrderWeight() > 0) {
        // Calculate shipping category
        Shipping cat = BeanUtils.calculateShippingCategory(user.getDeliveryAddressFacade().getCountry());
        order.setShippingCategory(cat);
        // Calculate shipping cost for the immediate shipment
        int weight = 0;
        for (CustomerOrderItem item : order.getItemsAsList()) {
          if (item.isBackIssue() || item.getStartIssue() == currentIssue.getIssueNumber()) {
            weight += item.getWeight();
          }
        }
        int cost = PostageManager.calculatePostage(cat, OrderType.MAGAZINES_ONLY, weight);
        // Subscriptions need extra shipment for the future deliveries
        for (CustomerOrderItem item : order.getItemsAsList()) {
          if (!item.isBackIssue()) {
            for (int i = (item.getStartIssue() > currentIssue.getIssueNumber() ? 0 : 1); i < item.getNumIssues(); i++) {
              cost += PostageManager.calculatePostage(cat, OrderType.MAGAZINES_ONLY, item.getWeight());
            }
          }
        }
        order.setShippingPrice(cost);
      } else {
        order.setShippingCategory(Shipping.NONE);
        order.setShippingPrice(0);
      }
    } catch (PostageException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      UserAccountManager.save(user);
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
    String server = ctx.getRequestScheme() + "://" + ctx.getRequestServerName() + ":" + ctx.getRequestServerPort() + ctx.getRequestContextPath();
    return server + PayPointCallbackServlet.PAYPOINT_PATH;
  }

  public String getPaypointBackCallback() {
    ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
    String server = ctx.getRequestScheme() + "://" + ctx.getRequestServerName() + ":" + ctx.getRequestServerPort() + ctx.getRequestContextPath();
    return server + "/basket.xhtml";
  }

  public String getPaypointOrderDetails() {
    NumberFormat format = NumberFormat.getInstance(Locale.UK);
    StringBuilder sb = new StringBuilder();
    sb.append("delimit=;+=x");
    for (CustomerOrderItem item : order.getItemsAsList()) {
      BigDecimal big = BigDecimal.valueOf(item.getPrice()).scaleByPowerOfTen(-2);
      String name = item.getName().replace('"', '\'').replace('+', '_').replace('=', '_').replace(';', '_');
      sb.append(";prod=");
      sb.append(name);
      sb.append("+item_amount=");
      sb.append(format.format(big.doubleValue()));
      sb.append("x");
      sb.append(item.getQuantity());
    }
    BigDecimal big = BigDecimal.valueOf(order.getShippingPrice()).scaleByPowerOfTen(-2);
    sb.append(";prod=SHIPPING+item_amount=");
    sb.append(format.format(big.doubleValue()));
    return sb.toString();
  }
}
