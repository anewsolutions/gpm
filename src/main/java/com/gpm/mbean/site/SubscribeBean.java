/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gpm.i18n.MessageProvider;
import com.gpm.manager.IssueManager;
import com.gpm.manager.exception.IssueException;
import com.gpm.model.Issue;
import com.gpm.model.IssueOrderItem;
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
      if (!issue.isEzineAvailable()) {
        format = Format.HCOPY;
      }
    } catch (IssueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public Issue getCurrentIssue() {
    return issue;
  }

  public String getEdition() {
    String edition = "";
    if (issue != null) {
      String published = BeanUtils.formatPublished(issue.getPublishedDate());
      edition = MessageProvider.getMessage("subThisIssueEdition", issue.getIssueNumber(), published);
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
      return MessageProvider.getMessage("subDesc" + getFormat() + getLength(), issue.getIssueNumber());
    } else {
      return "";
    }
  }

  public String getSubscriptionPrice() {
    String price = BeanUtils.formatPrice(Issue.currentPrice * getLength());
    return MessageProvider.getMessage("subPostageAndPackaging" + getFormat(), price);
  }

  public String buy() {
    // Build order item
    IssueOrderItem order = new IssueOrderItem();
    String published = BeanUtils.formatPublished(issue.getPublishedDate());
    order.setName(MessageProvider.getMessage("subShortDesc" + getFormat() + getLength(), issue.getIssueNumber(),
        published));
    order.setPrice(Issue.currentPrice * getLength());
    order.setQuantity(1);
    // Issue details
    order.setStartIssue(issue.getIssueNumber());
    order.setNumIssues(length);
    order.setFormat(format);
    order.setBackIssue(false);
    // Add to basket
    BasketBean basket = BeanUtils.fetchBasketBean();
    basket.addItemToBasket(order);
    return "/basket.xhtml?faces-redirect=true";
  }
}
