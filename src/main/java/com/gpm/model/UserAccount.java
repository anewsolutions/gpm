/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
  private String resetToken;
  private Date resetTokenExpiry;
  private boolean administrator = false;
  private UserAddress billingAddress = new UserAddress();
  private UserAddress deliveryAddress = new UserAddress();
  private boolean deliverySameAsBilling = true;
  private List<UserIssue> magazines = new ArrayList<UserIssue>(0);

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

  @Column(length = 64, nullable = true)
  public String getResetToken() {
    return resetToken;
  }

  public void setResetToken(final String resetToken) {
    this.resetToken = resetToken;
  }

  @Column(nullable = true)
  public Date getResetTokenExpiry() {
    return resetTokenExpiry;
  }

  public void setResetTokenExpiry(final Date resetTokenExpiry) {
    this.resetTokenExpiry = resetTokenExpiry;
  }

  public boolean isAdministrator() {
    return administrator;
  }

  public void setAdministrator(final boolean administrator) {
    this.administrator = administrator;
  }

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  public UserAddress getBillingAddress() {
    if (billingAddress == null) {
      billingAddress = new UserAddress();
    }
    return billingAddress;
  }

  public void setBillingAddress(final UserAddress billingAddress) {
    this.billingAddress = billingAddress;
  }

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  public UserAddress getDeliveryAddress() {
    if (deliveryAddress == null) {
      deliveryAddress = new UserAddress();
    }
    return deliveryAddress;
  }

  public void setDeliveryAddress(final UserAddress deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }

  public boolean isDeliverySameAsBilling() {
    return deliverySameAsBilling;
  }

  public void setDeliverySameAsBilling(final boolean deliverySameAsBilling) {
    this.deliverySameAsBilling = deliverySameAsBilling;
  }

  @Transient
  public UserAddress getDeliveryAddressFacade() {
    if (isDeliverySameAsBilling()) {
      return getBillingAddress();
    } else {
      return getDeliveryAddress();
    }
  }

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  public List<UserIssue> getMagazines() {
    return magazines;
  }

  public void setMagazines(final List<UserIssue> magazines) {
    this.magazines = magazines;
  }

  @Transient
  public void addMagazine(final UserIssue magazine) {
    boolean found = false;
    for (UserIssue mag : getMagazines()) {
      if (mag.getIssueNumber().equals(magazine.getIssueNumber()) && mag.getFormat().equals(magazine.getFormat())) {
        found = true;
        break;
      }
    }
    if (!found) {
      getMagazines().add(magazine);
    }
  }
}
