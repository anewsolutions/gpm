/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
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

  // Subscription attributes
  // TODO make the format an enum
  private String format = "ezine";
  private int length = 1;

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

  public String getFormat() {
    return format;
  }

  public void setFormat(final String format) {
    this.format = format;
  }

  public int getLength() {
    return length;
  }

  public void setLength(final int length) {
    this.length = length;
  }

  public String getSubscriptionDescription() {
    if (issue != null) {
      StringBuilder next = new StringBuilder();
      for (int i = issue.getNumber() + 1; i < issue.getNumber() + getLength(); i++) {
        if (next.length() != 0) {
          next.append(", ");
        }
        next.append(i);
      }
      if ("ezine".equals(getFormat())) {
        return MessageProvider.getMessage("subDesc" + getLength() + "Online", issue.getNumber(), next.toString());
      } else {
        return MessageProvider.getMessage("subDesc" + getLength() + "Physical", issue.getNumber(), next.toString());
      }
    } else {
      return "";
    }
  }

  public String getSubscriptionPrice() {
    Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    NumberFormat format = NumberFormat.getCurrencyInstance(locale);
    format.setCurrency(Currency.getInstance("GBP"));
    Float price = Issue.currentPrice * new Float(getLength());
    if ("ezine".equals(getFormat())) {
      return MessageProvider.getMessage("subNoPostageAndPackaging", format.format(price));
    } else {
      return MessageProvider.getMessage("subPostageAndPackaging", format.format(price));
    }
  }
}
