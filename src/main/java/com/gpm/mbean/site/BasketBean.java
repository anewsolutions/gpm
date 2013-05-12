/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.gpm.model.OrderItem;

@ManagedBean
@SessionScoped
public class BasketBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private List<OrderItem> items = new ArrayList<OrderItem>();

  public List<OrderItem> getAllBasketItems() {
    return items;
  }

  public void addItemToBasket(OrderItem item) {
    if (item != null) {
      items.add(item);
    }
  }

  public void removeItemFromBasket(OrderItem item) {
    if (item != null) {
      items.remove(item);
    }
  }

  public int getTotalUniqueBasketItems() {
    return items.size();
  }

  public int getTotalBasketItems() {
    int num = 0;
    Iterator<OrderItem> i = items.iterator();
    while (i.hasNext()) {
      OrderItem item = i.next();
      num += item.getQuantity();
    }
    return num;
  }

  public String getTotalBasketPrice() {
    int price = 0;
    Iterator<OrderItem> i = items.iterator();
    while (i.hasNext()) {
      OrderItem item = i.next();
      price += item.getPrice() * item.getQuantity();
    }
    return BeanUtils.formatPrice(price);
  }

  public String getItemPrice(OrderItem item) {
    return BeanUtils.formatPrice(item.getPrice());
  }
}
