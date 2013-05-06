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
import com.gpm.manager.ConfigurationManager;
import com.gpm.manager.IssueManager;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.manager.exception.IssueException;
import com.gpm.model.Configuration;
import com.gpm.model.Issue;

@ManagedBean
@ViewScoped
public class HomePageBean implements Serializable {
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

  public String getFacebookKey() {
    String key = "";
    try {
      Configuration config = ConfigurationManager.findByKey("facebook.key");
      key = config.getValue();
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return key;
  }

  public String getCurrentIssueCover() {
    String cover = "";
    if (issue != null) {
      cover = issue.getCoverImageUri();
    }
    return cover;
  }

  public String getCurrentIssueEdition() {
    String edition = "";
    if (issue != null) {
      Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
      SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", locale);
      String published = sdf.format(issue.getPublished());
      edition = MessageProvider.getMessage("indexEdition", issue.getNumber(), published);
    }
    return edition;
  }
}
