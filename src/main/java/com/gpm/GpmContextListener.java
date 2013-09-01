/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gpm.manager.PostageManager;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.PostageException;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.model.Postage;
import com.gpm.model.PostageBandCost;
import com.gpm.model.UserAccount;
import com.gpm.model.enums.OrderType;
import com.gpm.model.enums.Shipping;

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

    // Initialise shipping costs
    try {
      List<Postage> postages = PostageManager.findAllPostages();
      for (OrderType orderType : OrderType.values()) {
        for (Shipping shipping : Shipping.values()) {
          // Ignore special value NONE
          if (shipping == Shipping.NONE) {
            continue;
          }
          // Check to see if there are costs for this item type/shipping combination
          boolean found = false;
          for (Postage postage : postages) {
            if (postage.getOrderTypeCategory() == orderType && postage.getShippingCategory() == shipping) {
              found = true;
              break;
            }
          }
          // Create some if there are not
          if (!found) {
            Postage p = new Postage();
            p.setOrderTypeCategory(orderType);
            p.setShippingCategory(shipping);
            p.setBandCosts(generatePostageBandCosts(shipping));
            PostageManager.save(p);
          }
        }
      }
    } catch (PostageException e) {
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

  /**
   * Helper to populate initial values for shipping costs. Costs taken from 2013 prices on
   * http://www.royalmail.com/ plus 50 pence for packaging.
   */
  private static List<PostageBandCost> generatePostageBandCosts(final Shipping shipping) {
    List<PostageBandCost> bandCosts = new ArrayList<PostageBandCost>(8);
    for (int i = 0; i < 8; i++) {
      int weight = (i + 1) * 250;
      int cost = 50; // For packaging
      switch (shipping) {
      case FIRST_UK:
        if (weight <= 250) {
          cost += 120;
        } else if (weight <= 500) {
          cost += 160;
        } else if (weight <= 750) {
          cost += 230;
        } else if (weight <= 1000) {
          cost += 300;
        } else if (weight <= 1250) {
          cost += 685;
        } else if (weight <= 1500) {
          cost += 685;
        } else if (weight <= 1750) {
          cost += 685;
        } else {
          cost += 685;
        }
        break;
      case AIR_EUROPE:
        if (weight <= 250) {
          cost += 350;
        } else if (weight <= 500) {
          cost += 495;
        } else if (weight <= 750) {
          cost += 640;
        } else if (weight <= 1000) {
          cost += 785;
        } else if (weight <= 1250) {
          cost += 930;
        } else if (weight <= 1500) {
          cost += 1075;
        } else if (weight <= 1750) {
          cost += 1220;
        } else {
          cost += 1365;
        }
        break;
      case AIR_WORLD_1:
        if (weight <= 250) {
          cost += 450;
        } else if (weight <= 500) {
          cost += 720;
        } else if (weight <= 750) {
          cost += 990;
        } else if (weight <= 1000) {
          cost += 1260;
        } else if (weight <= 1250) {
          cost += 1530;
        } else if (weight <= 1500) {
          cost += 1800;
        } else if (weight <= 1750) {
          cost += 2070;
        } else {
          cost += 2340;
        }
        break;
      case AIR_WORLD_2:
        if (weight <= 250) {
          cost += 470;
        } else if (weight <= 500) {
          cost += 755;
        } else if (weight <= 750) {
          cost += 1040;
        } else if (weight <= 1000) {
          cost += 1325;
        } else if (weight <= 1250) {
          cost += 1610;
        } else if (weight <= 1500) {
          cost += 1895;
        } else if (weight <= 1750) {
          cost += 2180;
        } else {
          cost += 2465;
        }
        break;
      default:
        cost = 0;
        break;
      }
      PostageBandCost bandCost = new PostageBandCost();
      bandCost.setWeightBand(weight);
      bandCost.setWeightCost(cost);
      bandCosts.add(bandCost);
    }
    return bandCosts;
  }
}
