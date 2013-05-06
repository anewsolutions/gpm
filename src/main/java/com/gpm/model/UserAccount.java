/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * A JPA entity for a user account.
 * 
 * @author mbooth
 */
@Entity
public class UserAccount extends Base {
  private static final long serialVersionUID = 1L;

  private String email;
  private String name;
  private String passwordSalt;
  private String passwordHash;
  private String facebookIdent;
  private String facebookToken;
  private UserAddress billingAddress;
  private UserAddress shippingAddress;

  public UserAccount() {
    super();
  }

  @Column(unique = true, nullable = false)
  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  @Column(nullable = false)
  public String getName() {
    return name;
  }

  @Transient
  public String getShortName() {
    int i = getName().indexOf(' ');
    if (i != -1) {
      return getName().substring(0, i);
    } else {
      return getName();
    }
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Column(length = 64, nullable = true)
  public String getPasswordSalt() {
    return passwordSalt;
  }

  public void setPasswordSalt(final String passwordSalt) {
    this.passwordSalt = passwordSalt;
  }

  @Column(length = 128, nullable = true)
  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(final String passwordHash) {
    this.passwordHash = passwordHash;
  }

  @Column(length = 64, nullable = true)
  public String getFacebookIdent() {
    return facebookIdent;
  }

  public void setFacebookIdent(final String facebookIdent) {
    this.facebookIdent = facebookIdent;
  }

  @Column(nullable = true)
  public String getFacebookToken() {
    return facebookToken;
  }

  public void setFacebookToken(final String facebookToken) {
    this.facebookToken = facebookToken;
  }

  @OneToOne(fetch = FetchType.EAGER)
  public UserAddress getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(UserAddress billingAddress) {
    this.billingAddress = billingAddress;
  }

  @OneToOne(fetch = FetchType.EAGER)
  public UserAddress getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(UserAddress shippingAddress) {
    this.shippingAddress = shippingAddress;
  }
}
