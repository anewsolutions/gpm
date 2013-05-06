/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean.site;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class BeanUtils {
  public static String formatPrice(final int price) {
    Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    NumberFormat format = NumberFormat.getCurrencyInstance(locale);
    format.setCurrency(Currency.getInstance("GBP"));
    BigDecimal big = BigDecimal.valueOf(price).scaleByPowerOfTen(-2);
    return format.format(big.doubleValue());
  }

  public static String formatPublished(final Date published) {
    Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", locale);
    return sdf.format(published);
  }

  public static LoginBean fetchLoginBean() {
    HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    LoginBean bean = (LoginBean)req.getSession().getAttribute("loginBean");
    return bean;
  }
}
