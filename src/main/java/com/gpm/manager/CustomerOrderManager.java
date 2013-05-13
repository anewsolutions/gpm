/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.util.UUID;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.manager.exception.CustomerOrderException;
import com.gpm.model.CustomerOrder;

public class CustomerOrderManager {
  /**
   * Get the customer order with the given UUID.
   * 
   * @param uuid
   *          the UUID of the customer order requested
   * @return a customer order
   * @throws CustomerOrderException
   *           if there was a problem fetching the customer order
   */
  public static CustomerOrder findCustomerOrder(final String uuid) throws CustomerOrderException {
    try {
      return ControllerFactory.getCustomerOrderController().get(UUID.fromString(uuid));
    } catch (IllegalArgumentException e) {
      throw new CustomerOrderException(e);
    } catch (ControllerException e) {
      throw new CustomerOrderException(e);
    }
  }

  /**
   * Persist the given customer order to the data store.
   * 
   * @param order
   *          the customer order to be saved
   * @throws CustomerOrderException
   *           if there was a problem saving the customer order
   */
  public static void storeCustomerOrder(final CustomerOrder order) throws CustomerOrderException {
    try {
      ControllerFactory.getCustomerOrderController().save(order);
    } catch (ControllerException e) {
      throw new CustomerOrderException(e);
    }
  }
}
