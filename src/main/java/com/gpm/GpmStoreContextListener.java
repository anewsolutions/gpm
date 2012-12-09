/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.gpm.dao.DAOException;
import com.gpm.dao.ProductDAO;
import com.gpm.model.Product;

public class GpmStoreContextListener implements ServletContextListener {

  @Override
  public void contextDestroyed(ServletContextEvent event) {
  }

  @Override
  public void contextInitialized(ServletContextEvent event) {
    try {
      Product p = new Product();
      p.setName("Product A");
      p.setVariant("");
      p.setSku("123ABC");
      ProductDAO.getInstance().create(p);
    } catch (DAOException e) {
      e.printStackTrace();
    }
  }
}
