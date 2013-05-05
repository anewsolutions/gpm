/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.gpm.UploadsServlet;
import com.gpm.i18n.MessageProvider;
import com.gpm.manager.ProductManager;
import com.gpm.manager.exception.ProductException;
import com.gpm.model.Product;
import com.gpm.model.Variant;

@ManagedBean
@ViewScoped
public class ShopFrontBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private List<Product> products;
  private Map<Product, Variant> selectedVariants = new HashMap<Product, Variant>();

  @PostConstruct
  public void init() {
    // Pre-load products
    try {
      products = ProductManager.findAll();
    } catch (ProductException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public List<Product> getProducts() {
    return products;
  }

  public Variant getSelectedVariant(Product product) {
    // Get currently selected variant for this product
    Variant variant = selectedVariants.get(product);
    if (variant == null) {
      variant = product.getDefaultVariant();
      selectedVariants.put(product, variant);
    }
    return variant;
  }

  public Variant getSelectedVariant() {
    // Get the product currently in context
    FacesContext context = FacesContext.getCurrentInstance();
    Product product = context.getApplication().evaluateExpressionGet(context, "#{product}", Product.class);
    return getSelectedVariant(product);
  }

  public void setSelectedVariant(Variant variant) {
    // Get the product currently in context
    FacesContext context = FacesContext.getCurrentInstance();
    Product product = context.getApplication().evaluateExpressionGet(context, "#{product}", Product.class);

    // Set the currently selected variant for this product
    selectedVariants.put(product, variant);
  }

  public String getVariantImage(Product product) {
    Variant variant = getSelectedVariant(product);
    if (variant.isHasImage()) {
      return UploadsServlet.UPLOADS_PATH + variant.getImageFilename();
    } else {
      variant = product.getDefaultVariant();
      if (variant.isHasImage()) {
        return UploadsServlet.UPLOADS_PATH + variant.getImageFilename();
      } else {
        return "http://placehold.it/245x245";
      }
    }
  }

  public String getVariantPrice(Product product) {
    Variant variant = getSelectedVariant(product);
    return BeanUtils.formatPrice(variant.getPrice());
  }

  public int getVariantStock(Product product) {
    Variant variant = getSelectedVariant(product);
    return variant.getStock();
  }

  public String getVariantStockText(Product product) {
    Variant variant = getSelectedVariant(product);
    if (variant.getStock() == 0) {
      return MessageProvider.getMessage("shopStockNone");
    } else if (variant.getStock() < 10) {
      return MessageProvider.getMessage("shopStockSome", variant.getStock());
    } else {
      return MessageProvider.getMessage("shopStockMany");
    }
  }
}
