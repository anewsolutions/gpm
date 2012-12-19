/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.gpm.controller.CategoryController;
import com.gpm.controller.ControllerException;
import com.gpm.controller.ProductController;
import com.gpm.model.Category;
import com.gpm.model.Product;

public class GpmStoreContextListener implements ServletContextListener {

  @Override
  public void contextDestroyed(ServletContextEvent event) {
  }

  @Override
  public void contextInitialized(ServletContextEvent event) {
    try {
      Category c = new Category();
      c.setName("Test");
      CategoryController.getInstance().create(c);
      
      Product p = new Product();
      p.setName("Product A");
      p.setCode("123ABC");
//      p.setVariant("Red");
//      p.setDescription("This is a very nice product indeed.");
      ProductController.getInstance().create(p);
      p.getCategories().add(c);
      ProductController.getInstance().update(p);
    } catch (ControllerException e) {
      e.printStackTrace();
    }
  }
}
