/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.gpm.i18n.MessageProvider;
import com.gpm.manager.IssueManager;
import com.gpm.manager.exception.IssueException;
import com.gpm.model.Issue;
import com.gpm.model.IssueOrderItem;
import com.gpm.model.enums.Format;

@ManagedBean
@ViewScoped
public class BackIssueBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private List<Issue> issues = new ArrayList<Issue>();
  private Map<Issue, Format> formats = new HashMap<Issue, Format>();

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

  public String getEdition() {
    FacesContext context = FacesContext.getCurrentInstance();
    Issue issue = context.getApplication().evaluateExpressionGet(context, "#{issue}", Issue.class);
    String published = BeanUtils.formatPublished(issue.getPublishedDate());
    return MessageProvider.getMessage("backIssueEdition", issue.getIssueNumber(), published);
  }

  private Format getFormat(final Issue issue) {
    Format format = formats.get(issue);
    if (format == null) {
      // Use ezine as the default, if available
      if (issue.isEzineAvailable()) {
        format = Format.EZINE;
      } else {
        format = Format.HCOPY;
      }
      formats.put(issue, format);
    }
    return format;
  }

  public String getFormat() {
    FacesContext context = FacesContext.getCurrentInstance();
    Issue issue = context.getApplication().evaluateExpressionGet(context, "#{issue}", Issue.class);
    return getFormat(issue).name();
  }

  public void setFormat(final String format) {
    FacesContext context = FacesContext.getCurrentInstance();
    Issue issue = context.getApplication().evaluateExpressionGet(context, "#{issue}", Issue.class);
    formats.put(issue, Format.valueOf(format));
  }

  public void buy(final Issue issue) {
    // Build order item
    IssueOrderItem order = new IssueOrderItem();
    String published = BeanUtils.formatPublished(issue.getPublishedDate());
    order.setName(MessageProvider.getMessage("backShortDesc" + getFormat(issue), issue.getIssueNumber(), published));
    order.setPrice(Issue.backIssuePrice);
    order.setQuantity(1);
    // Issue details
    order.setStartIssue(issue.getIssueNumber());
    order.setNumIssues(1);
    order.setFormat(getFormat(issue));
    order.setBackIssue(true);
    // Add to basket
    BasketBean basket = BeanUtils.fetchBasketBean();
    basket.addItemToBasket(order);
  }
}
