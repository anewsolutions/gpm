/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.gpm.i18n.MessageProvider;
import com.gpm.manager.IssueManager;
import com.gpm.manager.exception.IssueException;
import com.gpm.mbean.BeanUtils;
import com.gpm.model.CustomerOrderItem;
import com.gpm.model.Issue;
import com.gpm.model.enums.Format;

@ManagedBean
@ViewScoped
public class SubscribeBean implements Serializable {
  private static final long serialVersionUID = 1L;

  // Subscription attributes
  private Issue issue = null;
  private int offset = 0;
  private int length = 1;
  private Format format = Format.EZINE;

  @ManagedProperty("#{basketBean}")
  private BasketBean basket;

  public BasketBean getBasket() {
    return basket;
  }

  public void setBasket(final BasketBean basket) {
    this.basket = basket;
  }

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

  public int getOffset() {
    return offset;
  }

  public void setOffset(final int offset) {
    this.offset = offset;
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
      return MessageProvider.getMessage(
          "subDesc" + getFormat() + Integer.toString(getLength()) + Integer.toString(getOffset()),
          issue.getIssueNumber() + getOffset());
    } else {
      return "";
    }
  }

  public String getSubscriptionPrice() {
    String price = "";
    if (format == Format.EZINE) {
      price = BeanUtils.formatPrice(issue.getEzinePrice() * getLength());
    } else {
      price = BeanUtils.formatPrice(issue.getHcopyPrice() * getLength());
    }
    return MessageProvider.getMessage("subPostageAndPackaging" + getFormat(), getLength(), price);
  }

  public String buy() {
    // Build order item
    CustomerOrderItem order = new CustomerOrderItem();
    order.setName(MessageProvider.getMessage("subShortDesc" + getFormat() + getLength(), issue.getIssueNumber()
        + getOffset()));
    if (format == Format.EZINE) {
      order.setPrice(issue.getEzinePrice() * getLength());
      order.setWeight(0);
    } else {
      order.setPrice(issue.getHcopyPrice() * getLength());
      order.setWeight(issue.getWeight());
    }
    order.setQuantity(1);
    // Issue details
    order.setStartIssue(issue.getIssueNumber() + getOffset());
    order.setNumIssues(length);
    order.setFormat(format);
    order.setBackIssue(false);
    // Add to basket
    basket.addItemToBasket(order);
    return "/basket.xhtml?faces-redirect=true";
  }
}
