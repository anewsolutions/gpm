/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class GpmContextListener implements ServletContextListener {

  @Override
  public void contextDestroyed(ServletContextEvent event) {
  }

  @Override
  public void contextInitialized(ServletContextEvent event) {
  }
}
