/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.gpm.model.enums.OrderType;
import com.gpm.model.enums.Shipping;

/**
 * A JPA entity for storing postage costs.
 * 
 * @author mbooth
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "orderTypeCategory", "shippingCategory" }))
public class Postage extends Base {
  private static final long serialVersionUID = 1L;

  private OrderType orderTypeCategory;
  private Shipping shippingCategory;
  private List<PostageBandCost> bandCosts = new ArrayList<PostageBandCost>();

  public Postage() {
    super();
  }

  @Column(nullable = false)
  public OrderType getOrderTypeCategory() {
    return orderTypeCategory;
  }

  public void setOrderTypeCategory(final OrderType orderTypeCategory) {
    this.orderTypeCategory = orderTypeCategory;
  }

  @Column(nullable = false)
  public Shipping getShippingCategory() {
    return shippingCategory;
  }

  public void setShippingCategory(final Shipping shippingCategory) {
    this.shippingCategory = shippingCategory;
  }

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  public List<PostageBandCost> getBandCosts() {
    return bandCosts;
  }

  public void setBandCosts(final List<PostageBandCost> bandCosts) {
    this.bandCosts = bandCosts;
  }
}
