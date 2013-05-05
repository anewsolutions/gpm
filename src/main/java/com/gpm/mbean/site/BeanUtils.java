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
}
