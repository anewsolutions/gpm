/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.controller;

import java.util.HashMap;
import java.util.Map;

import com.gpm.model.Base;
import com.gpm.model.Product;
import com.gpm.model.UserAccount;
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
  public static Controller<UserAccount> getUserAccountController() {
    if (!controllers.containsKey(UserAccount.class)) {
      controllers.put(UserAccount.class, new Controller<UserAccount>(UserAccount.class));
    }
    return (Controller<UserAccount>) controllers.get(UserAccount.class);
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
