/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.controller;

import java.util.HashMap;
import java.util.Map;

import com.gpm.model.Base;
import com.gpm.model.Configuration;
import com.gpm.model.CustomerOrder;
import com.gpm.model.Issue;
import com.gpm.model.Product;
import com.gpm.model.UserAccount;
import com.gpm.model.UserAddress;
import com.gpm.model.Variant;

/**
 * A factory for building instances of JPA entity controllers. A factory method is
 * available for each JPA entity type in the model.
 * 
 * @author mbooth
 */
public class ControllerFactory {

  private static Map<Class<? extends Base>, Controller<? extends Base>> controllers = new HashMap<Class<? extends Base>, Controller<? extends Base>>();

  @SuppressWarnings("unchecked")
  public static Controller<Configuration> getConfigurationController() {
    if (!controllers.containsKey(Configuration.class)) {
      controllers.put(Configuration.class, new Controller<Configuration>(Configuration.class));
    }
    return (Controller<Configuration>) controllers.get(Configuration.class);
  }

  @SuppressWarnings("unchecked")
  public static Controller<CustomerOrder> getCustomerOrderController() {
    if (!controllers.containsKey(CustomerOrder.class)) {
      controllers.put(CustomerOrder.class, new Controller<CustomerOrder>(CustomerOrder.class));
    }
    return (Controller<CustomerOrder>) controllers.get(CustomerOrder.class);
  }

  @SuppressWarnings("unchecked")
  public static Controller<Issue> getIssueController() {
    if (!controllers.containsKey(Issue.class)) {
      controllers.put(Issue.class, new Controller<Issue>(Issue.class));
    }
    return (Controller<Issue>) controllers.get(Issue.class);
  }

  @SuppressWarnings("unchecked")
  public static Controller<UserAccount> getUserAccountController() {
    if (!controllers.containsKey(UserAccount.class)) {
      controllers.put(UserAccount.class, new Controller<UserAccount>(UserAccount.class));
    }
    return (Controller<UserAccount>) controllers.get(UserAccount.class);
  }

  @SuppressWarnings("unchecked")
  public static Controller<UserAddress> getUserAddressController() {
    if (!controllers.containsKey(UserAddress.class)) {
      controllers.put(UserAddress.class, new Controller<UserAddress>(UserAddress.class));
    }
    return (Controller<UserAddress>) controllers.get(UserAddress.class);
  }

  @SuppressWarnings("unchecked")
  public static Controller<Product> getProductController() {
    if (!controllers.containsKey(Product.class)) {
      controllers.put(Product.class, new Controller<Product>(Product.class));
    }
    return (Controller<Product>) controllers.get(Product.class);
  }

  @SuppressWarnings("unchecked")
  public static Controller<Variant> getVariantController() {
    if (!controllers.containsKey(Variant.class)) {
      controllers.put(Variant.class, new Controller<Variant>(Variant.class));
    }
    return (Controller<Variant>) controllers.get(Variant.class);
  }
}
