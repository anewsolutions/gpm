/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.gpm.model.CustomerOrder;
import com.gpm.model.CustomerOrderItem;

@ManagedBean
@SessionScoped
public class BasketBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private CustomerOrder order = new CustomerOrder();

  public CustomerOrder getOrder() {
    return order;
  }

  public List<CustomerOrderItem> getAllBasketItems() {
    return order.getItemsAsList();
  }

  public void addItemToBasket(CustomerOrderItem item) {
    if (item != null) {
      order.addItem(item);
    }
  }

  public void removeItemFromBasket(CustomerOrderItem item) {
    if (item != null) {
      order.getItems().remove(item);
    }
  }

  public int getTotalBasketItems() {
    return order.getNumTotalItems();
  }

  public String getTotalBasketPrice() {
    return BeanUtils.formatPrice(order.getTotalOrderPrice());
  }

  public String getBasketItemPrice(CustomerOrderItem item) {
    return BeanUtils.formatPrice(item.getPrice());
  }
}
