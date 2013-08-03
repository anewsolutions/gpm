/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.UserAccount;

@WebListener
public class GpmContextListener implements ServletContextListener {

  private Log log = LogFactory.getLog(GpmContextListener.class);

  private EmailThread emailDelivery = new EmailThread();

  @Override
  public void contextInitialized(final ServletContextEvent event) {
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

    // Start threads
    emailDelivery.start();
  }

  @Override
  public void contextDestroyed(final ServletContextEvent event) {
    // Stop threads
    try {
      emailDelivery.stop();
    } catch (InterruptedException e) {
      log.warn("Thread interrupted whilst waiting for email delivery thread to stop");
    }
  }
}
