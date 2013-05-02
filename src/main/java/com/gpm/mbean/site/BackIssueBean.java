/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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
public class BackIssueBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private List<Issue> issues = new ArrayList<Issue>();

  @PostConstruct
  public void init() {
    try {
      issues.addAll(IssueManager.findBackIssues());
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public List<Issue> getIssues() {
    return issues;
  }

  public String getIssueEdition(Issue issue) {
    String edition = "";
    if (issue != null) {
      Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
      SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", locale);
      String published = sdf.format(issue.getPublished());
      edition = MessageProvider.getMessage("backIssueEdition", issue.getNumber(), published);
    }
    return edition;
  }
}
