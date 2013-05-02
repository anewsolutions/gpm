/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.PostConstruct;
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

  private Issue issue = null;

  @PostConstruct
  public void init() {
    try {
      issue = IssueManager.findCurrentIssue();
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

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
    if (issue != null) {
      Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
      SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", locale);
      String published = sdf.format(issue.getPublished());
      edition = MessageProvider.getMessage("subThisIssueEdition", issue.getNumber(), published);
    }
    return edition;
  }
}
