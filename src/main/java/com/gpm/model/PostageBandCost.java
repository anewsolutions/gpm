/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A JPA entity for storing postage weight band costs.
 * 
 * @author mbooth
 */
@Entity
public class PostageBandCost extends Base {
  private static final long serialVersionUID = 1L;

  private Integer weightBand;
  private Integer weightCost;

  public PostageBandCost() {
    super();
  }

  @Column(nullable = false)
  public Integer getWeightBand() {
    return weightBand;
  }

  public void setWeightBand(final Integer weightBand) {
    this.weightBand = weightBand;
  }

  @Column(nullable = false)
  public Integer getWeightCost() {
    return weightCost;
  }

  public void setWeightCost(final Integer weightCost) {
    this.weightCost = weightCost;
  }
}
