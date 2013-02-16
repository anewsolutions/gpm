/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.shop;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.gpm.model.Basket;
import com.gpm.model.BasketItem;
import com.gpm.model.Product;
import com.gpm.model.Variant;

@ManagedBean
@SessionScoped
public class BasketBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private Basket basket = new Basket();

  /**
   * JSF method to add a product and the selected variant to the shopping basket.
   */
  public void addItemToBasket(Product product, Variant variant) {
    if (product != null && variant != null) {
      basket.addBasketItem(product, variant);
    }
  }

  /**
   * JSF method to get the total number of individual items in the shopping basket.
   * 
   * @return the total number of basket items
   */
  public int getNumTotalBasketItems() {
    return basket.getNumTotalBasketItems();
  }

  public List<BasketItem> getAllBasketItems() {
    return basket.getBasketItemsAsList();
  }
}
