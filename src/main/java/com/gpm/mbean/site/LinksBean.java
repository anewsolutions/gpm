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

import com.gpm.manager.LinkManager;
import com.gpm.manager.exception.LinkException;
import com.gpm.model.Link;

@ManagedBean
@ViewScoped
public class LinksBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private List<Link> links = new ArrayList<Link>();

  @PostConstruct
  public void init() {
    try {
      links.addAll(LinkManager.findAllLinks());
    } catch (LinkException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public List<Link> getLinks() {
    return links;
  }
}
