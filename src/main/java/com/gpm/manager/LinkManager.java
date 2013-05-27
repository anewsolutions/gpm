/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.util.List;
import java.util.UUID;

import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.manager.exception.LinkException;
import com.gpm.model.Link;

public class LinkManager {
  /**
   * Get the link with the given UUID.
   * 
   * @param uuid
   *          the UUID of the link requested
   * @return a link or null if no link was found for the given UUID
   * @throws LinkException
   *           if there was a problem fetching the link
   */
  public static Link findByUuid(final String uuid) throws LinkException {
    try {
      return ControllerFactory.getLinkController().get(UUID.fromString(uuid));
    } catch (IllegalArgumentException e) {
      throw new LinkException(e);
    } catch (ControllerException e) {
      throw new LinkException(e);
    }
  }

  /**
   * Get all links.
   * 
   * @return the list of links
   * @throws LinkException
   *           if there was a problem fetching the list of links
   */
  public static List<Link> findAllLinks() throws LinkException {
    try {
      return ControllerFactory.getLinkController().getAll();
    } catch (ControllerException e) {
      throw new LinkException(e);
    }
  }

  /**
   * Persist the given link to the data store.
   * 
   * @param link
   *          the link to be saved
   * @throws LinkException
   *           if there was a problem saving the link
   */
  public static void save(final Link link) throws LinkException {
    try {
      ControllerFactory.getLinkController().save(link);
    } catch (ControllerException e) {
      throw new LinkException(e);
    }
  }

  /**
   * Delete the given link from the data store.
   * 
   * @param link
   *          the link to be deleted
   * @throws LinkException
   *           if there was a problem deleting the link
   */
  public static void delete(final Link link) throws LinkException {
    try {
      ControllerFactory.getLinkController().delete(link.getUuid());
    } catch (ControllerException e) {
      throw new LinkException(e);
    }
  }
}
