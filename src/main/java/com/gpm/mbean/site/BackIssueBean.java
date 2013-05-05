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
import javax.servlet.http.HttpSession;

import com.gpm.i18n.MessageProvider;
import com.gpm.manager.IssueManager;
import com.gpm.manager.exception.IssueException;
import com.gpm.model.BackIssueItem;
import com.gpm.model.Issue;
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
    String published = BeanUtils.formatPublished(issue.getPublished());
    return MessageProvider.getMessage("backIssueEdition", issue.getNumber(), published);
  }

  private Format getFormat(final Issue issue) {
    Format format = formats.get(issue);
    if (format == null) {
      // Use ezine as the default, if available
      if (issue.isEzine()) {
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
    // Build order
    BackIssueItem order = new BackIssueItem();
    String published = BeanUtils.formatPublished(issue.getPublished());
    order.setName(MessageProvider.getMessage("backShortDesc" + getFormat(issue), issue.getNumber(), published));
    order.setPrice(Issue.backIssuePrice);
    order.setQuantity(1);
    // Back issue details
    order.setIssue(issue.getUuid());
    order.setFormat(getFormat(issue));
    // Add to basket
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    BasketBean basket = (BasketBean) session.getAttribute("basketBean");
    basket.addItemToBasket(order);
  }
}
