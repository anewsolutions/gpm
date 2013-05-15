/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.gpm.i18n.MessageProvider;
import com.gpm.manager.IssueManager;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.IssueException;
import com.gpm.manager.exception.UserAccountException;
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

  public String readOnline(final Issue issue) {
    return "";
  }
}
