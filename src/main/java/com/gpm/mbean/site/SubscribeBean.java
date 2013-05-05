/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.gpm.i18n.MessageProvider;
import com.gpm.manager.IssueManager;
import com.gpm.manager.exception.IssueException;
import com.gpm.model.Issue;
import com.gpm.model.SubscriptionItem;
import com.gpm.model.enums.Format;

@ManagedBean
@ViewScoped
public class SubscribeBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private Issue issue = null;

  // Subscription attributes
  private int length = 1;
  private Format format = Format.EZINE;

  @PostConstruct
  public void init() {
    try {
      issue = IssueManager.findCurrentIssue();
      if (!issue.isEzine()) {
        format = Format.HCOPY;
      }
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

  public String getEdition() {
    String edition = "";
    if (issue != null) {
      String published = BeanUtils.formatPublished(issue.getPublished());
      edition = MessageProvider.getMessage("subThisIssueEdition", issue.getNumber(), published);
    }
    return edition;
  }

  public int getLength() {
    return length;
  }

  public void setLength(final int length) {
    this.length = length;
  }

  public String getFormat() {
    return format.name();
  }

  public void setFormat(final String format) {
    this.format = Format.valueOf(format);
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
      return MessageProvider.getMessage("subDesc" + getFormat() + getLength(), issue.getNumber(), next.toString());
    } else {
      return "";
    }
  }

  public String getSubscriptionPrice() {
    String price = BeanUtils.formatPrice(Issue.currentPrice * getLength());
    return MessageProvider.getMessage("subPostageAndPackaging" + getFormat(), price);
  }

  public String buy() {
    // Build order
    SubscriptionItem order = new SubscriptionItem();
    String published = BeanUtils.formatPublished(issue.getPublished());
    order.setName(MessageProvider.getMessage("subShortDesc" + getFormat() + getLength(), issue.getNumber(), published));
    order.setPrice(Issue.currentPrice * getLength());
    order.setQuantity(1);
    // Subscription details
    order.setFirst(issue.getUuid());
    order.setLength(length);
    order.setFormat(format);
    // Add to basket
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    BasketBean basket = (BasketBean) session.getAttribute("basketBean");
    basket.addItemToBasket(order);
    return "/basket.xhtml?faces-redirect=true";
  }
}
