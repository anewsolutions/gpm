/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.gpm.manager.CustomerOrderManager;
import com.gpm.manager.exception.CustomerOrderException;
import com.gpm.model.CustomerOrder;
import com.gpm.model.CustomerOrderItem;

@ManagedBean
@SessionScoped
public class BasketBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private CustomerOrder order;

  public void clearOrder() {
    order = null;
  }

  public CustomerOrder getOrder() {
    if (order == null) {
      HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
      try {
        order = CustomerOrderManager.findOrCreateBySessionId(session.getId());
      } catch (CustomerOrderException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
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

  public int getTotalBasketItems() {
    return getOrder().getNumTotalItems();
  }

  public String getTotalBasketPrice() {
    return BeanUtils.formatPrice(getOrder().getTotalOrderPrice());
  }

  public String getBasketItemPrice(CustomerOrderItem item) {
    return BeanUtils.formatPrice(item.getPrice());
  }
}
