/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.gpm.model.enums.Shipping;

@Entity
public class CustomerOrder extends Base {
  private static final long serialVersionUID = 1L;

  // Order details
  private Set<CustomerOrderItem> items = new HashSet<CustomerOrderItem>(0);
  private UserAccount user;
  private Shipping shippingCategory;
  private Integer shippingPrice;

  // Paypoint transaction details
  private String authCode;
  private String errorCode;
  private String errorMessage;

  public CustomerOrder() {
    super();
  }

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  public Set<CustomerOrderItem> getItems() {
    return items;
  }

  @Transient
  public List<CustomerOrderItem> getItemsAsList() {
    List<CustomerOrderItem> items = new ArrayList<CustomerOrderItem>(getItems());
    Collections.sort(items);
    return items;
  }

  public void setItems(final Set<CustomerOrderItem> items) {
    this.items = items;
  }

  @Transient
  public void addItem(final CustomerOrderItem item) {
    // If the item is already in the order, increment quantity
    boolean done = false;
    Iterator<CustomerOrderItem> it = getItems().iterator();
    while (it.hasNext()) {
      CustomerOrderItem i = it.next();
      if (item.getName().equals(i.getName())) {
        i.setQuantity(i.getQuantity() + 1);
        done = true;
        break;
      }
    }
    if (!done) {
      getItems().add(item);
    }
  }

  @Transient
  public int getNumUniqueItems() {
    return getItems().size();
  }

  @Transient
  public int getNumTotalItems() {
    int num = 0;
    Iterator<CustomerOrderItem> it = getItems().iterator();
    while (it.hasNext()) {
      num += it.next().getQuantity();
    }
    return num;
  }

  @Transient
  public int getTotalOrderPrice() {
    int price = 0;
    Iterator<CustomerOrderItem> it = getItems().iterator();
    while (it.hasNext()) {
      CustomerOrderItem i = it.next();
      price += (i.getPrice() * i.getQuantity());
    }
    return price;
  }

  @Transient
  public int getTotalOrderWeight() {
    int weight = 0;
    Iterator<CustomerOrderItem> it = getItems().iterator();
    while (it.hasNext()) {
      CustomerOrderItem i = it.next();
      weight += (i.getWeight() * i.getQuantity());
    }
    return weight;
  }
  
  @ManyToOne(fetch = FetchType.EAGER)
  public UserAccount getUser() {
    return user;
  }

  public void setUser(final UserAccount user) {
    this.user = user;
  }

  @Column(nullable = false)
  public Shipping getShippingCategory() {
    return shippingCategory;
  }

  public void setShippingCategory(final Shipping shippingCategory) {
    this.shippingCategory = shippingCategory;
  }

  @Column(nullable = false)
  public Integer getShippingPrice() {
    return shippingPrice;
  }

  public void setShippingPrice(final Integer shippingPrice) {
    this.shippingPrice = shippingPrice;
  }

  @Transient
  public int getGrandTotal() {
    return getTotalOrderPrice() + getShippingPrice();
  }

  public String getAuthCode() {
    return authCode;
  }

  public void setAuthCode(final String authCode) {
    this.authCode = authCode;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(final String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(final String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
