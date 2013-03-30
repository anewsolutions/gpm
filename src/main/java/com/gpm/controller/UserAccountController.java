/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.controller;

import com.gpm.model.UserAccount;

/**
 * An entity controller for user accounts.
 * 
 * @author mbooth
 * 
 * @see UserAccount
 */
public class UserAccountController extends GenericController<UserAccount> {

  /**
   * The singleton instance.
   */
  private static UserAccountController _instance;

  /**
   * Protected constructor for singleton pattern.
   */
  protected UserAccountController() {
    super(UserAccount.class);
  }

  /**
   * Get the singleton instance.
   * 
   * @return the instance of the controller
   */
  public static UserAccountController getInstance() {
    if (_instance == null) {
      _instance = new UserAccountController();
    }
    return _instance;
  }
}
