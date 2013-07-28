/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.UserAccount;

@WebListener
public class GpmContextListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent event) {
    // User address migration
    // TODO: Remove this after first production deployment
    try {
      List<UserAccount> accounts = UserAccountManager.findAllUserAccounts();
      for (UserAccount account : accounts) {
        if (account.getBillingAddress().getUuid().equals(account.getDeliveryAddress().getUuid())) {
          account.setDeliverySameAsBilling(true);
          account.setDeliveryAddress(null);
        } else {
          account.setDeliverySameAsBilling(false);
        }
        UserAccountManager.save(account);
      }
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
  }
}
