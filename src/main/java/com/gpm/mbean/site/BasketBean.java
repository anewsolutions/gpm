/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.gpm.manager.CustomerOrderManager;
import com.gpm.manager.exception.CustomerOrderException;
import com.gpm.mbean.BeanUtils;
import com.gpm.model.CustomerOrder;
import com.gpm.model.CustomerOrderItem;

@ManagedBean
@ViewScoped
public class BasketBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private CustomerOrder order;

  @PostConstruct
  public void init() {
    try {
      HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
      order = CustomerOrderManager.findOrCreateBySessionId(session.getId());
    } catch (CustomerOrderException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public CustomerOrder getOrder() {
    return order;
  }

  private void storeOrder() {
    try {
      CustomerOrderManager.storeCustomerOrder(order);
    } catch (CustomerOrderException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public List<CustomerOrderItem> getAllBasketItems() {
    return getOrder().getItemsAsList();
  }

  public void addItemToBasket(final CustomerOrderItem item) {
    if (item != null) {
      getOrder().addItem(item);
      storeOrder();
    }
  }

  public void removeItemFromBasket(final CustomerOrderItem item) {
    if (item != null) {
      getOrder().getItems().remove(item);
      storeOrder();
    }
  }

  public int getQuantity() {
    FacesContext context = FacesContext.getCurrentInstance();
    CustomerOrderItem item = context.getApplication().evaluateExpressionGet(context, "#{basketItem}", CustomerOrderItem.class);
    return item.getQuantity();
  }

  public void setQuantity(final int quantity) {
    FacesContext context = FacesContext.getCurrentInstance();
    CustomerOrderItem item = context.getApplication().evaluateExpressionGet(context, "#{basketItem}", CustomerOrderItem.class);
    if (quantity > item.getQuantity()) {
      addItemToBasket(item);
    }
    if (quantity < item.getQuantity()) {
      item.setQuantity(quantity);
      storeOrder();
    }
  }

  public int getTotalBasketItems() {
    return getOrder().getNumTotalItems();
  }

  public String getTotalBasketPrice() {
    return BeanUtils.formatPrice(getOrder().getTotalOrderPrice());
  }

  public String getBasketItemPrice(final CustomerOrderItem item) {
    return BeanUtils.formatPrice(item.getPrice());
  }
}
