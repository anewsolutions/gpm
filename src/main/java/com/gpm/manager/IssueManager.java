/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.gpm.UploadsServlet;
import com.gpm.controller.ControllerException;
import com.gpm.controller.ControllerFactory;
import com.gpm.controller.ControllerFilter;
import com.gpm.manager.exception.IssueException;
import com.gpm.model.Issue;

public class IssueManager {
  /**
   * Get the issue with the given UUID.
   * 
   * @param uuid
   *          the UUID of the issue of the magazine requested
   * @return an issue of the magazine or null if no issue was found for the given UUID
   * @throws IssueException
   *           if there was a problem fetching the issue
   */
  public static Issue findByUuid(final String uuid) throws IssueException {
    try {
      return ControllerFactory.getIssueController().get(UUID.fromString(uuid));
    } catch (IllegalArgumentException e) {
      throw new IssueException(e);
    } catch (ControllerException e) {
      throw new IssueException(e);
    }
  }

  /**
   * Get all issues of a magazine.
   * 
   * @return the list of issues including all back issues, the current issue and issues
   *         yet to be published
   * @throws IssueException
   *           if there was a problem fetching the list of issues
   */
  public static List<Issue> findAllIssues() throws IssueException {
    try {
      return ControllerFactory.getIssueController().getAll("publishedDate", false);
    } catch (ControllerException e) {
      throw new IssueException(e);
    }
  }

  /**
   * Get the current issue of a magazine.
   * 
   * @return the most recent issue whose published date is in the past
   * @throws IssueException
   *           if there was a problem fetching the current issue
   */
  public static Issue findCurrentIssue() throws IssueException {
    try {
      List<ControllerFilter> filters = new ArrayList<ControllerFilter>();
      filters.add(new ControllerFilter("publishedDate", "<=", new Date()));
      List<Issue> issues = ControllerFactory.getIssueController().getAll("publishedDate", false, filters);
      // Only interested in the latest published issue, so return only first one
      if (!issues.isEmpty()) {
        return issues.get(0);
      } else {
        return null;
      }
    } catch (ControllerException e) {
      throw new IssueException(e);
    }
  }

  /**
   * Get all the back issues for a magazine.
   * 
   * @return the list of issues whose published date is in the past, except for the most
   *         recently published issue, which is considered the current issue rather than a
   *         back issue
   * @throws IssueException
   *           if there was a problem fetching the list of back issues
   */
  public static List<Issue> findBackIssues() throws IssueException {
    try {
      List<ControllerFilter> filters = new ArrayList<ControllerFilter>();
      filters.add(new ControllerFilter("publishedDate", "<=", new Date()));
      List<Issue> issues = ControllerFactory.getIssueController().getAll("publishedDate", false, filters);
      // Not interested in the latest published issue, so truncate the first in the list
      if (!issues.isEmpty()) {
        issues.remove(0);
      }
      return issues;
    } catch (ControllerException e) {
      throw new IssueException(e);
    }
  }

  /**
   * Persist the given issue to the data store.
   * 
   * @param issue
   *          the issue to be saved
   * @throws IssueException
   *           if there was a problem saving the issue
   */
  public static void save(final Issue issue) throws IssueException {
    try {
      String coverImage = issue.getCoverImage();
      if (coverImage != null && coverImage.startsWith("tmp-")) {
        // Cover image has temporary name, so we need to move it
        issue.setCoverImage(coverImage.substring(4));
        File oldImage = new File(UploadsServlet.getUploadsDirectory(), coverImage);
        File newImage = new File(UploadsServlet.getUploadsDirectory(), issue.getCoverImage());
        FileUtils.copyFile(oldImage, newImage, false);
        FileUtils.forceDelete(oldImage);
      }
      ControllerFactory.getIssueController().save(issue);
    } catch (ControllerException e) {
      throw new IssueException(e);
    } catch (IOException e) {
      throw new IssueException(e);
    }
  }

  /**
   * Delete the given issue from the data store.
   * 
   * @param issue
   *          the issue to be deleted
   * @throws IssueException
   *           if there was a problem deleting the issue
   */
  public static void delete(final Issue issue) throws IssueException {
    try {
      ControllerFactory.getIssueController().delete(issue.getUuid());
    } catch (ControllerException e) {
      throw new IssueException(e);
    }
  }
}
