/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;

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

  public void setName(final String name) {
    this.name = name;
  }

  @Column(length = 64, nullable = false)
  public String getPasswordSalt() {
    return passwordSalt;
  }

  public void setPasswordSalt(final String passwordSalt) {
    this.passwordSalt = passwordSalt;
  }

  @Column(length = 128, nullable = false)
  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(final String passwordHash) {
    this.passwordHash = passwordHash;
  }
}
