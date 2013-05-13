/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A JPA entity for storing an address associated with a user account.
 * 
 * @author mbooth
 */
@Entity
public class UserAddress extends Base {
  private static final long serialVersionUID = 1L;

  private String name;
  private String company;
  private String address1;
  private String address2;
  private String city;
  private String state;
  private String country;
  private String postcode;

  public UserAddress() {
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
  public String getCompany() {
    return company;
  }

  public void setCompany(final String company) {
    this.company = company;
  }

  @Column(nullable = false)
  public String getAddress1() {
    return address1;
  }

  public void setAddress1(final String address1) {
    this.address1 = address1;
  }

  @Column(nullable = false)
  public String getAddress2() {
    return address2;
  }

  public void setAddress2(final String address2) {
    this.address2 = address2;
  }

  @Column(nullable = false)
  public String getCity() {
    return city;
  }

  public void setCity(final String city) {
    this.city = city;
  }

  @Column(nullable = false)
  public String getState() {
    return state;
  }

  public void setState(final String state) {
    this.state = state;
  }

  @Column(nullable = false)
  public String getCountry() {
    return country;
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  @Column(nullable = false)
  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(final String postcode) {
    this.postcode = postcode;
  }
}
