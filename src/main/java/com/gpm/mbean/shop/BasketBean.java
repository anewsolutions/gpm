/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.shop;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.gpm.model.Basket;
import com.gpm.model.Product;
import com.gpm.model.Variant;

@ManagedBean
@SessionScoped
public class BasketBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private Product product;
  private Variant variant;

  private Basket basket = new Basket();

  /**
   * JSF method to set which product we are about to add to the basket.
   * <p>
   * TODO This could probably be removed when we move to EL 2.2, because we would be able
   * to pass parameters directly into addItemToBasket.
   * 
   * @param product
   */
  public void setProduct(Product product) {
    this.product = product;
  }

  /**
   * JSF method to set which product variant we are about to add to the basket.
   * <p>
   * TODO This could probably be removed when we move to EL 2.2, because we would be able
   * to pass parameters directly into addItemToBasket.
   * 
   * @param variant
   */
  public void setVariant(Variant variant) {
    this.variant = variant;
  }

  /**
   * JSF method to add a product and the selected variant to the shopping basket.
   */
  public void addItemToBasket() {
    basket.addBasketItem(product, variant);
  }

  /**
   * JSF method to get the total number of individual items in the shopping basket.
   * 
   * @return the total number of basket items
   */
  public int getNumTotalBasketItems() {
    return basket.getNumTotalBasketItems();
  }
}
