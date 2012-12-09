/**
 * Copyright 2012 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.admin;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.gpm.dao.DAOException;
import com.gpm.dao.ProductDAO;
import com.gpm.model.Product;

@ManagedBean
@SessionScoped
public class ProductAdminBean {

  public List<Product> getAll() {
    List<Product> all = null;
    try {
      all = ProductDAO.getInstance().getAll(null, true);
    } catch (DAOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return all;
  }
}
