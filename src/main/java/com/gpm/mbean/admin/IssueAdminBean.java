/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.gpm.manager.IssueManager;
import com.gpm.manager.exception.IssueException;
import com.gpm.model.Issue;

@ManagedBean
@ViewScoped
public class IssueAdminBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private boolean editing;
  private Issue selected;

  /**
   * Bean initialisation.
   */
  @PostConstruct
  public void init() {
    try {
      // Load the selected item, if an ID is passed as a query parameter
      FacesContext fc = FacesContext.getCurrentInstance();
      String uuid = fc.getExternalContext().getRequestParameterMap().get("uuid");
      if (uuid != null && !uuid.isEmpty()) {
        selected = IssueManager.findByUuid(uuid);
        editing = true;
      } else {
        selected = new Issue();
      }
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * JSF method to determine whether we are editing an issue or adding a new issue.
   * 
   * @return true if editing an existing issue, false if adding a new issue
   */
  public boolean isEditing() {
    return editing;
  }

  /**
   * JSF method to provide access to the currently selected issue.
   * 
   * @return the selected issue
   */
  public Issue getSelected() {
    return selected;
  }

  /**
   * JSF method to provide access to all issues.
   * 
   * @return a list of issues
   */
  public List<Issue> getAll() {
    List<Issue> all = null;
    try {
      all = IssueManager.findAllIssues();
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }

  /**
   * JSF method to save the selected issue.
   */
  public String save() {
    try {
      IssueManager.save(selected);
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/issue_list?faces-redirect=true";
  }

  /**
   * JSF method to delete the selected issue.
   */
  public String delete() {
    try {
      IssueManager.delete(selected);
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "/secure/admin/issue_list?faces-redirect=true";
  }
}
