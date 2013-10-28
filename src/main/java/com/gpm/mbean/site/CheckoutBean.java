/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

import com.gpm.PayPalCallbackServlet;
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
import com.gpm.model.enums.PaymentMethod;
import com.gpm.model.enums.Shipping;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

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
      HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
      order = CustomerOrderManager.findOrCreateBySessionId(session.getId());
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (CustomerOrderException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
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
      CustomerOrderManager.storeCustomerOrder(order);
    } catch (PostageException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (CustomerOrderException e) {
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

  public String getPaymentMethod() {
    if (order.getPaymentMethod() != null) {
      return order.getPaymentMethod().toString();
    }
    return PaymentMethod.PAYPOINT.toString();
  }

  public void setPaymentMethod(final String paymentMethod) {
    order.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
  }

  public String finishCheckout2() {
    try {
      CustomerOrderManager.storeCustomerOrder(order);
    } catch (CustomerOrderException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (PaymentMethod.PAYPOINT.toString().equals(getPaymentMethod())) {
      return "/secure/checkout_paypoint.xhtml?faces-redirect=true";
    } else {
      return "/secure/checkout_paypal.xhtml?faces-redirect=true";
    }
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
    String server = ctx.getRequestScheme() + "://" + ctx.getRequestServerName() + ":" + ctx.getRequestServerPort()
        + ctx.getRequestContextPath() + PayPointCallbackServlet.PAYPOINT_PATH;
    return server;
  }

  public String getPaypalCallback() {
    ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
    String server = ctx.getRequestScheme() + "://" + ctx.getRequestServerName() + ":" + ctx.getRequestServerPort()
        + ctx.getRequestContextPath() + PayPalCallbackServlet.PAYPAL_PATH + "?trans_id=" + order.getUuid().toString();
    return server;
  }

  public String getBackCallback() {
    ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
    String server = ctx.getRequestScheme() + "://" + ctx.getRequestServerName() + ":" + ctx.getRequestServerPort()
        + ctx.getRequestContextPath();
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

  public void processPaypalOrder() {
    NumberFormat format = NumberFormat.getInstance(Locale.UK);
    format.setMinimumFractionDigits(2);
    format.setMaximumFractionDigits(2);
    BigDecimal big;

    Payment payment = new Payment();

    Details amountDetails = new Details();
    big = BigDecimal.valueOf(order.getShippingPrice()).scaleByPowerOfTen(-2);
    amountDetails.setShipping(format.format(big.doubleValue()));
    big = BigDecimal.valueOf(order.getTotalOrderPrice()).scaleByPowerOfTen(-2);
    amountDetails.setSubtotal(format.format(big.doubleValue()));

    Amount amount = new Amount();
    amount.setCurrency("GBP");
    big = BigDecimal.valueOf(order.getGrandTotal()).scaleByPowerOfTen(-2);
    amount.setTotal(format.format(big.doubleValue()));
    amount.setDetails(amountDetails);

    RedirectUrls redirectUrls = new RedirectUrls();
    redirectUrls.setCancelUrl(getBackCallback());
    redirectUrls.setReturnUrl(getPaypalCallback());

    ItemList itemList = new ItemList();
    List<Item> items = new ArrayList<Item>(order.getItemsAsList().size());
    for (CustomerOrderItem orderItem : order.getItemsAsList()) {
      big = BigDecimal.valueOf(orderItem.getPrice()).scaleByPowerOfTen(-2);
      Item item = new Item();
      item.setPrice(format.format(big.doubleValue()));
      item.setCurrency("GBP");
      item.setQuantity(orderItem.getQuantity().toString());
      String name = orderItem.getName().replace('"', '\'').replace('(', '\'').replace(')', '\'').replace(';', ',');
      item.setName(name);
      items.add(item);
    }
    itemList.setItems(items);

    Transaction transaction = new Transaction();
    transaction.setAmount(amount);
    transaction.setItemList(itemList);
    transaction.setDescription("Guinea Pig Magazine Order Ref " + order.getUuid().toString());
    List<Transaction> transactions = new ArrayList<Transaction>();
    transactions.add(transaction);

    Payer payer = new Payer();
    payer.setPaymentMethod("paypal");

    payment.setIntent("sale");
    payment.setPayer(payer);
    payment.setRedirectUrls(redirectUrls);
    payment.setTransactions(transactions);

    // Get oauth token for PayPal API access
    String token = "";
    try {
      Properties props = ConfigurationManager.findPropsByKey("paypal");
      String key = props.getProperty("paypal.key");
      String secret = props.getProperty("paypal.secret");
      token = new OAuthTokenCredential(key, secret).getAccessToken();
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (PayPalRESTException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    String redirectUrl = null;
    try {
      payment = payment.create(token);
      order.setPaymentId(payment.getId());
      List<Links> links = payment.getLinks();
      for (Links l : links) {
        if (l.getRel().equalsIgnoreCase("approval_url")) {
          redirectUrl = URLDecoder.decode(l.getHref(), "UTF-8");
          break;
        }
      }
      CustomerOrderManager.storeCustomerOrder(order);
    } catch (PayPalRESTException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (CustomerOrderException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    try {
      externalContext.redirect(redirectUrl);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
