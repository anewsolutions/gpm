/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.gpm.model.enums.Format;

@Entity
public class CustomerOrderItem extends Base {
  private static final long serialVersionUID = 1L;

  private String name;
  private Integer quantity;
  private Integer price;
  private Integer weight;

  // TODO: Refactor these out into a generic key/value pairs associated with an item
  private Integer startIssue;
  private Integer numIssues;
  private Format format;
  private boolean backIssue;

  public CustomerOrderItem() {
    super();
  }

  @Column(nullable = false)
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Column(nullable = false)
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(final Integer quantity) {
    this.quantity = quantity;
  }

  @Column(nullable = false)
  public Integer getPrice() {
    return price;
  }

  public void setPrice(final Integer price) {
    this.price = price;
  }

  @Column(nullable = false)
  public Integer getWeight() {
    return weight;
  }

  public void setWeight(final Integer weight) {
    this.weight = weight;
  }

  @Column(nullable = false)
  public Integer getStartIssue() {
    return startIssue;
  }

  public void setStartIssue(final Integer startIssue) {
    this.startIssue = startIssue;
  }

  @Column(nullable = false)
  public Integer getNumIssues() {
    return numIssues;
  }

  public void setNumIssues(final Integer numIssues) {
    this.numIssues = numIssues;
  }

  @Column(nullable = false)
  public Format getFormat() {
    return format;
  }

  public void setFormat(final Format format) {
    this.format = format;
  }

  public boolean isBackIssue() {
    return backIssue;
  }

  public void setBackIssue(final boolean backIssue) {
    this.backIssue = backIssue;
  }
}
