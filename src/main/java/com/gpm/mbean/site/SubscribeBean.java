/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.gpm.i18n.MessageProvider;
import com.gpm.manager.IssueManager;
import com.gpm.manager.exception.IssueException;
import com.gpm.model.Issue;

@ManagedBean
@ViewScoped
public class SubscribeBean implements Serializable {
  private static final long serialVersionUID = 1L;

  public Issue getCurrentIssue() {
    Issue issue = null;
    try {
      issue = IssueManager.findCurrentIssue();
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return issue;
  }

  public String getCurrentIssueEdition() {
    String edition = "";
    Issue issue = null;
    try {
      issue = IssueManager.findCurrentIssue();
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (issue != null) {
      Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
      edition = MessageProvider.getMessage("subThisIssueEdition", issue.getNumber(), issue.getPublishedForLocale(locale));
    }
    return edition;
  }
}
