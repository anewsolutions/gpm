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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * A JPA entity for a shopping basket.
 * 
 * @author mbooth
 */
@Entity
public class Basket extends Base {
  private static final long serialVersionUID = 1L;

  private Set<BasketItem> basketItems = new HashSet<BasketItem>(0);

  public Basket() {
    super();
  }

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  public Set<BasketItem> getBasketItems() {
    return basketItems;
  }

  public void setBasketItems(Set<BasketItem> basketItems) {
    this.basketItems = basketItems;
  }

  @Transient
  public int getNumUniqueBasketItems() {
    return getBasketItems().size();
  }

  @Transient
  public int getNumTotalBasketItems() {
    int num = 0;
    Iterator<BasketItem> i = getBasketItems().iterator();
    while (i.hasNext()) {
      num += i.next().getQuantity();
    }
    return num;
  }

  @Transient
  public List<BasketItem> getBasketItemsAsList() {
    List<BasketItem> items = new ArrayList<BasketItem>(getBasketItems());
    Collections.sort(items);
    return items;
  }

  @Transient
  public void addBasketItem(Product product, Variant variant) {
    // If the product is already in the basket, increment quantity
    boolean done = false;
    Iterator<BasketItem> i = getBasketItems().iterator();
    while (i.hasNext()) {
      BasketItem item = i.next();
      if (item.getProduct().equals(product) && item.getVariant().equals(variant)) {
        // Don't add more than we have in stock
        if (item.getQuantity() < variant.getStock()) {
          item.setQuantity(item.getQuantity() + 1);
        }
        done = true;
        break;
      }
    }
    // If not already basket, add new item
    if (!done) {
      BasketItem item = new BasketItem();
      item.setProduct(product);
      item.setVariant(variant);
      item.setQuantity(1);
      getBasketItems().add(item);
    }
  }
}
