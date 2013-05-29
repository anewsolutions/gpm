/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.gpm.calameo.CalameoClient;
import com.gpm.calameo.CalameoConfig;
import com.gpm.calameo.CalameoException;
import com.gpm.calameo.Session;
import com.gpm.calameo.impl.CalameoClientImpl;
import com.gpm.calameo.impl.CalameoConfigImpl;
import com.gpm.i18n.MessageProvider;
import com.gpm.manager.ConfigurationManager;
import com.gpm.manager.IssueManager;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.manager.exception.IssueException;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.mbean.BeanUtils;
import com.gpm.model.Configuration;
import com.gpm.model.Issue;
import com.gpm.model.UserAccount;
import com.gpm.model.UserIssue;
import com.gpm.model.enums.Format;

@ManagedBean
@ViewScoped
public class MagazineBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private UserAccount user;
  private List<Issue> issues = new ArrayList<Issue>();

  @PostConstruct
  public void init() {
    try {
      user = UserAccountManager.findCurrentlyLoggedIn();
      List<Issue> allIssues = IssueManager.findAllIssues();
      for (Issue anIssue : allIssues) {
        for (UserIssue magazine : user.getMagazines()) {
          if (magazine.getFormat() != Format.EZINE) {
            continue;
          }
          if (anIssue.getIssueNumber() == magazine.getIssueNumber()) {
            issues.add(anIssue);
          }
        }
      }
    } catch (UserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public List<Issue> getIssues() {
    return issues;
  }

  public String getEdition() {
    FacesContext context = FacesContext.getCurrentInstance();
    Issue issue = context.getApplication().evaluateExpressionGet(context, "#{issue}", Issue.class);
    String published = BeanUtils.formatPublished(issue.getPublishedDate());
    return MessageProvider.getMessage("mymagsIssueEdition", issue.getIssueNumber(), published);
  }

  public void readOnline(final Issue issue) {
    // Configure calameo client
    CalameoConfig calcon = new CalameoConfigImpl();
    CalameoClient calcli = new CalameoClientImpl(calcon);
    try {
      Configuration key = ConfigurationManager.findByKey("calameo.key");
      Configuration secret = ConfigurationManager.findByKey("calameo.secret");
      Configuration subscription = ConfigurationManager.findByKey("calameo.subscription");
      calcon.setKey(key.getConfigValue());
      calcon.setSecret(secret.getConfigValue());
      calcon.setSubscription(subscription.getConfigValue());
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    String login = user.getUuid().toString();
    String book = issue.getEzineLink();
    String extra = "Generated at " + new Date();

    // Create user if necessary
    try {
      calcli.getSubscriber(login);
    } catch (CalameoException e) {
      // If no such user, try creating one
      if (e.getCode() == 601) {
        try {
          calcli.addSubscriber(login, login, user.getShortName(), user.getName(), user.getEmail(), true, extra);
        } catch (CalameoException ex) {
          // TODO Auto-generated catch block
          ex.printStackTrace();
          return;
        }
      }
    }
    // Create drm if necessary
    try {
      calcli.addSubscriberSingleDrm(login, book, extra);
    } catch (CalameoException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // Authorise the user
    Session session = null;
    try {
      session = calcli.authSubscriberSession(login);
    } catch (CalameoException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // Redirect
    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    try {
      ec.redirect("http://en.calameo.com/read/" + book + "?subid=" + session.getId());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
