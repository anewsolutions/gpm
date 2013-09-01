/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.controller.ControllerFilter;
import com.gpm.manager.exception.PostageException;
import com.gpm.model.Postage;
import com.gpm.model.PostageBandCost;
import com.gpm.model.enums.OrderType;
import com.gpm.model.enums.Shipping;

public class PostageManager {
  /**
   * Get the postage costs with the given UUID.
   * 
   * @param uuid
   *          the UUID of the postage requested
   * @return a postage or null if no postage was found for the given UUID
   * @throws PostageException
   *           if there was a problem fetching the postage
   */
  public static Postage findByUuid(final String uuid) throws PostageException {
    try {
      return ControllerFactory.getPostageController().get(UUID.fromString(uuid));
    } catch (IllegalArgumentException e) {
      throw new PostageException(e);
    } catch (ControllerException e) {
      throw new PostageException(e);
    }
  }

  /**
   * Get all postage costs.
   * 
   * @return the list of postage costs
   * @throws PostageException
   *           if there was a problem fetching the list of postage costs
   */
  public static List<Postage> findAllPostages() throws PostageException {
    try {
      return ControllerFactory.getPostageController().getAll();
    } catch (ControllerException e) {
      throw new PostageException(e);
    }
  }

  /**
   * Get all postage costs for a given order type.
   * 
   * @param orderType
   *          the type of order
   * @return the list of postage costs
   * @throws PostageException
   *           if there was a problem fetching the list of postage costs
   */
  public static List<Postage> findAllByOrderType(final OrderType orderType) throws PostageException {
    try {
      List<ControllerFilter> filters = new ArrayList<ControllerFilter>();
      filters.add(new ControllerFilter("orderTypeCategory", "=", orderType));
      return ControllerFactory.getPostageController().getAll(filters);
    } catch (ControllerException e) {
      throw new PostageException(e);
    }
  }

  public static int calculatePostage(final Shipping shipping, final OrderType orderType, final int weight) throws PostageException {
    try {
      List<ControllerFilter> filters = new ArrayList<ControllerFilter>();
      filters.add(new ControllerFilter("orderTypeCategory", "=", orderType));
      filters.add(new ControllerFilter("shippingCategory", "=", shipping));
      List<Postage> postages = ControllerFactory.getPostageController().getAll(filters);
      // Unique constraints should mean we only get one back
      Postage postage = null;
      if (postages != null && !postages.isEmpty()) {
        postage = postages.get(0);
      } else {
        throw new PostageException("Unable to calculate postage for shipping " + shipping + " and order type " + orderType);
      }
      // Calculate postage weight band return that cost
      int cost = 0;
      for (PostageBandCost bandCost : postage.getBandCosts()) {
        if (weight <= bandCost.getWeightBand() && cost <= bandCost.getWeightCost()) {
          cost = bandCost.getWeightCost();
        }
      }
      return cost;
    } catch (ControllerException e) {
      throw new PostageException(e);
    }
  }

  /**
   * Persist the given postage to the data store.
   * 
   * @param postage
   *          the postage to be saved
   * @throws PostageException
   *           if there was a problem saving the postage
   */
  public static void save(final Postage postage) throws PostageException {
    try {
      ControllerFactory.getPostageController().save(postage);
    } catch (ControllerException e) {
      throw new PostageException(e);
    }
  }

  /**
   * Delete the given postage from the data store.
   * 
   * @param postage
   *          the postage to be deleted
   * @throws PostageException
   *           if there was a problem deleting the postage
   */
  public static void delete(final Postage postage) throws PostageException {
    try {
      ControllerFactory.getPostageController().delete(postage.getUuid());
    } catch (ControllerException e) {
      throw new PostageException(e);
    }
  }
}
