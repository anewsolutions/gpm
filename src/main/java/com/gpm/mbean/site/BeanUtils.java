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

import com.gpm.model.enums.Shipping;

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

  /**
   * Utility to get the login bean from the session.
   * 
   * @return the login bean
   */
  public static LoginBean fetchLoginBean() {
    HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    LoginBean bean = (LoginBean) req.getSession().getAttribute("loginBean");
    return bean;
  }

  /**
   * Utility to get the basket bean from the session.
   * 
   * @return the basket bean
   */
  public static BasketBean fetchBasketBean() {
    HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    BasketBean bean = (BasketBean) req.getSession().getAttribute("basketBean");
    return bean;
  }

  private static String[] uk = { "UNITED KINGDOM", "ISLE OF MAN" };
  private static String[] europe = { "ÅLAND ISLANDS", "ALBANIA", "ANDORRA", "ARMENIA", "AUSTRIA", "AZERBAIJAN",
      "BELARUS", "BELGIUM", "BOSNIA AND HERZEGOVINA", "BULGARIA", "CROATIA", "CYPRUS", "CZECH REPUBLIC", "DENMARK",
      "ESTONIA", "FAROE ISLANDS", "FINLAND", "FRANCE", "GEORGIA", "GERMANY", "GIBRALTAR", "GREECE", "GREENLAND",
      "GUERNSEY", "HOLY SEE (VATICAN CITY STATE)", "HUNGARY", "ICELAND", "IRELAND", "ITALY", "JERSEY", "KAZAKHSTAN",
      "KOSOVO", "KYRGYZSTAN", "LATVIA", "LIECHTENSTEIN", "LITHUANIA", "LUXEMBOURG",
      "MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF", "MALTA", "MOLDOVA, REPUBLIC OF", "MONACO", "MONTENEGRO",
      "NETHERLANDS", "NORWAY", "POLAND", "PORTUGAL", "ROMANIA", "RUSSIAN FEDERATION", "SAN MARINO", "SERBIA",
      "SLOVAKIA", "SLOVENIA", "SPAIN", "SVALBARD AND JAN MAYEN", "SWEDEN", "SWITZERLAND", "TAJIKISTAN", "TURKEY",
      "TURKMENISTAN", "UKRAINE", "UZBEKISTAN", };
  private static String[] world2 = { "AMERICAN SAMOA", "AUSTRALIA", "PALAU", "CHINA", "FIJI", "FRENCH POLYNESIA",
      "FRENCH SOUTHERN TERRITORIES", "GUAM", "JAPAN", "KIRIBATI", "KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF",
      "KOREA, REPUBLIC OF", "MARSHALL ISLANDS", "MICRONESIA, FEDERATED STATES OF", "MONGOLIA", "NAURU",
      "NEW CALEDONIA", "NEW ZEALAND", "NORFOLK ISLAND", "NORTHERN MARIANA ISLANDS", "PAPUA NEW GUINEA", "PHILIPPINES",
      "PITCAIRN", "SAMOA", "SOLOMON ISLANDS", "TAIWAN, PROVINCE OF CHINA", "TIMOR-LESTE", "TOKELAU", "TONGA", "TUVALU",
      "VANUATU", "VIRGIN ISLANDS, BRITISH", "VIRGIN ISLANDS, U.S.", "WALLIS AND FUTUNA", };

  /**
   * This is the ISO 3166 list of countries.
   * 
   * @return an array of countries suitable for UI drop down lists
   * @see http://www.iso.org/iso/country_codes
   */
  public static String[] generateCountriesList() {
    String[] countries = { "AFGHANISTAN", "ÅLAND ISLANDS", "ALBANIA", "ALGERIA", "AMERICAN SAMOA", "ANDORRA", "ANGOLA",
        "ANGUILLA", "ANTARCTICA", "ANTIGUA AND BARBUDA", "ARGENTINA", "ARMENIA", "ARUBA", "AUSTRALIA", "AUSTRIA",
        "AZERBAIJAN", "BAHAMAS", "BAHRAIN", "BANGLADESH", "BARBADOS", "BELARUS", "BELGIUM", "BELIZE", "BENIN",
        "BERMUDA", "BHUTAN", "BOLIVIA, PLURINATIONAL STATE OF", "BONAIRE, SINT EUSTATIUS AND SABA",
        "BOSNIA AND HERZEGOVINA", "BOTSWANA", "BOUVET ISLAND", "BRAZIL", "BRITISH INDIAN OCEAN TERRITORY",
        "BRUNEI DARUSSALAM", "BULGARIA", "BURKINA FASO", "BURUNDI", "CAMBODIA", "CAMEROON", "CANADA", "CAPE VERDE",
        "CAYMAN ISLANDS", "CENTRAL AFRICAN REPUBLIC", "CHAD", "CHILE", "CHINA", "CHRISTMAS ISLAND",
        "COCOS (KEELING) ISLANDS", "COLOMBIA", "COMOROS", "CONGO", "CONGO, THE DEMOCRATIC REPUBLIC OF THE",
        "COOK ISLANDS", "COSTA RICA", "CÔTE D'IVOIRE", "CROATIA", "CUBA", "CURAÇAO", "CYPRUS", "CZECH REPUBLIC",
        "DENMARK", "DJIBOUTI", "DOMINICA", "DOMINICAN REPUBLIC", "ECUADOR", "EGYPT", "EL SALVADOR",
        "EQUATORIAL GUINEA", "ERITREA", "ESTONIA", "ETHIOPIA", "FALKLAND ISLANDS (MALVINAS)", "FAROE ISLANDS", "FIJI",
        "FINLAND", "FRANCE", "FRENCH GUIANA", "FRENCH POLYNESIA", "FRENCH SOUTHERN TERRITORIES", "GABON", "GAMBIA",
        "GEORGIA", "GERMANY", "GHANA", "GIBRALTAR", "GREECE", "GREENLAND", "GRENADA", "GUADELOUPE", "GUAM",
        "GUATEMALA", "GUERNSEY", "GUINEA", "GUINEA-BISSAU", "GUYANA", "HAITI", "HEARD ISLAND AND MCDONALD ISLANDS",
        "HOLY SEE (VATICAN CITY STATE)", "HONDURAS", "HONG KONG", "HUNGARY", "ICELAND", "INDIA", "INDONESIA",
        "IRAN, ISLAMIC REPUBLIC OF", "IRAQ", "IRELAND", "ISLE OF MAN", "ISRAEL", "ITALY", "JAMAICA", "JAPAN", "JERSEY",
        "JORDAN", "KAZAKHSTAN", "KENYA", "KIRIBATI", "KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF", "KOREA, REPUBLIC OF",
        "KOSOVO", "KUWAIT", "KYRGYZSTAN", "LAO PEOPLE'S DEMOCRATIC REPUBLIC", "LATVIA", "LEBANON", "LESOTHO",
        "LIBERIA", "LIBYA", "LIECHTENSTEIN", "LITHUANIA", "LUXEMBOURG", "MACAO",
        "MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF", "MADAGASCAR", "MALAWI", "MALAYSIA", "MALDIVES", "MALI", "MALTA",
        "MARSHALL ISLANDS", "MARTINIQUE", "MAURITANIA", "MAURITIUS", "MAYOTTE", "MEXICO",
        "MICRONESIA, FEDERATED STATES OF", "MOLDOVA, REPUBLIC OF", "MONACO", "MONGOLIA", "MONTENEGRO", "MONTSERRAT",
        "MOROCCO", "MOZAMBIQUE", "MYANMAR", "NAMIBIA", "NAURU", "NEPAL", "NETHERLANDS", "NEW CALEDONIA", "NEW ZEALAND",
        "NICARAGUA", "NIGER", "NIGERIA", "NIUE", "NORFOLK ISLAND", "NORTHERN MARIANA ISLANDS", "NORWAY", "OMAN",
        "PAKISTAN", "PALAU", "PALESTINE, STATE OF", "PANAMA", "PAPUA NEW GUINEA", "PARAGUAY", "PERU", "PHILIPPINES",
        "PITCAIRN", "POLAND", "PORTUGAL", "PUERTO RICO", "QATAR", "RÉUNION", "ROMANIA", "RUSSIAN FEDERATION", "RWANDA",
        "SAINT BARTHÉLEMY", "SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA", "SAINT KITTS AND NEVIS", "SAINT LUCIA",
        "SAINT MARTIN (FRENCH PART)", "SAINT PIERRE AND MIQUELON", "SAINT VINCENT AND THE GRENADINES", "SAMOA",
        "SAN MARINO", "SAO TOME AND PRINCIPE", "SAUDI ARABIA", "SENEGAL", "SERBIA", "SEYCHELLES", "SIERRA LEONE",
        "SINGAPORE", "SINT MAARTEN (DUTCH PART)", "SLOVAKIA", "SLOVENIA", "SOLOMON ISLANDS", "SOMALIA", "SOUTH AFRICA",
        "SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS", "SOUTH SUDAN", "SPAIN", "SRI LANKA", "SUDAN", "SURINAME",
        "SVALBARD AND JAN MAYEN", "SWAZILAND", "SWEDEN", "SWITZERLAND", "SYRIAN ARAB REPUBLIC",
        "TAIWAN, PROVINCE OF CHINA", "TAJIKISTAN", "TANZANIA, UNITED REPUBLIC OF", "THAILAND", "TIMOR-LESTE", "TOGO",
        "TOKELAU", "TONGA", "TRINIDAD AND TOBAGO", "TUNISIA", "TURKEY", "TURKMENISTAN", "TURKS AND CAICOS ISLANDS",
        "TUVALU", "UGANDA", "UKRAINE", "UNITED ARAB EMIRATES", "UNITED KINGDOM", "UNITED STATES",
        "UNITED STATES MINOR OUTLYING ISLANDS", "URUGUAY", "UZBEKISTAN", "VANUATU",
        "VENEZUELA, BOLIVARIAN REPUBLIC OF", "VIET NAM", "VIRGIN ISLANDS, BRITISH", "VIRGIN ISLANDS, U.S.",
        "WALLIS AND FUTUNA", "WESTERN SAHARA", "YEMEN", "ZAMBIA", "ZIMBABWE", };
    return countries;
  }

  /**
   * Calculate which Royal Mail shipping category a country is.
   * 
   * @param country
   *          a country from the ISO 3166 list of countries
   * @return a shipping category
   */
  public static Shipping calculateShippingCategory(final String country) {
    for (String match : BeanUtils.uk) {
      if (match.equals(country)) {
        return Shipping.FIRST_UK;
      }
    }
    for (String match : BeanUtils.europe) {
      if (match.equals(country)) {
        return Shipping.AIR_EUROPE;
      }
    }
    for (String match : BeanUtils.world2) {
      if (match.equals(country)) {
        return Shipping.AIR_WORLD_2;
      }
    }
    return Shipping.AIR_WORLD_1;
  }

  public static int calculateShippingCost(final Shipping category, final int weight) {
    int cost = -1;
    switch (category) {
    case FIRST_UK:
      if (weight <= 250) {
        cost = 120;
      } else if (weight <= 500) {
        cost = 160;
      } else if (weight <= 750) {
        cost = 230;
      } else if (weight <= 1000) {
        cost = 300;
      } else if (weight <= 1250) {
        cost = 685;
      } else if (weight <= 1500) {
        cost = 685;
      } else if (weight <= 1750) {
        cost = 685;
      } else if (weight <= 2000) {
        cost = 685;
      }
      break;
    case AIR_EUROPE:
      if (weight <= 250) {
        cost = 350;
      } else if (weight <= 500) {
        cost = 495;
      } else if (weight <= 750) {
        cost = 640;
      } else if (weight <= 1000) {
        cost = 785;
      } else if (weight <= 1250) {
        cost = 930;
      } else if (weight <= 1500) {
        cost = 1075;
      } else if (weight <= 1750) {
        cost = 1220;
      } else if (weight <= 2000) {
        cost = 1365;
      }
      break;
    case AIR_WORLD_1:
      if (weight <= 250) {
        cost = 450;
      } else if (weight <= 500) {
        cost = 720;
      } else if (weight <= 750) {
        cost = 990;
      } else if (weight <= 1000) {
        cost = 1260;
      } else if (weight <= 1250) {
        cost = 1530;
      } else if (weight <= 1500) {
        cost = 1800;
      } else if (weight <= 1750) {
        cost = 2070;
      } else if (weight <= 2000) {
        cost = 2340;
      }
      break;
    case AIR_WORLD_2:
      if (weight <= 250) {
        cost = 470;
      } else if (weight <= 500) {
        cost = 755;
      } else if (weight <= 750) {
        cost = 1040;
      } else if (weight <= 1000) {
        cost = 1325;
      } else if (weight <= 1250) {
        cost = 1610;
      } else if (weight <= 1500) {
        cost = 1895;
      } else if (weight <= 1750) {
        cost = 2180;
      } else if (weight <= 2000) {
        cost = 2465;
      }
      break;
    default:
      cost = 0;
      break;
    }
    return cost;
  }
}
