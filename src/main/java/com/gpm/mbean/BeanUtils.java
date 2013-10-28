/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.mbean;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.gpm.mbean.site.LoginBean;
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
  @Deprecated
  public static LoginBean fetchLoginBean() {
    HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    LoginBean bean = (LoginBean) req.getSession().getAttribute("loginBean");
    return bean;
  }

  private static Map<String, String> uk;
  private static Map<String, String> europe;
  private static Map<String, String> world1;
  private static Map<String, String> world2;

  static {
    uk = new HashMap<String, String>();
    europe = new HashMap<String, String>();
    world1 = new HashMap<String, String>();
    world2 = new HashMap<String, String>();
    world1.put("AFGHANISTAN", "AF");
    europe.put("ÅLAND ISLANDS", "AX");
    europe.put("ALBANIA", "AL");
    world1.put("ALGERIA", "DZ");
    world2.put("AMERICAN SAMOA", "AS");
    europe.put("ANDORRA", "AD");
    world1.put("ANGOLA", "AO");
    world1.put("ANGUILLA", "AI");
    world1.put("ANTARCTICA", "AQ");
    world1.put("ANTIGUA AND BARBUDA", "AG");
    world1.put("ARGENTINA", "AR");
    europe.put("ARMENIA", "AM");
    world1.put("ARUBA", "AW");
    world2.put("AUSTRALIA", "AU");
    europe.put("AUSTRIA", "AT");
    europe.put("AZERBAIJAN", "AZ");
    world1.put("BAHAMAS", "BS");
    world1.put("BAHRAIN", "BH");
    world1.put("BANGLADESH", "BD");
    world1.put("BARBADOS", "BB");
    europe.put("BELARUS", "BY");
    europe.put("BELGIUM", "BE");
    world1.put("BELIZE", "BZ");
    world1.put("BENIN", "BJ");
    world1.put("BERMUDA", "BM");
    world1.put("BHUTAN", "BT");
    world1.put("BOLIVIA, PLURINATIONAL STATE OF", "BO");
    world1.put("BONAIRE, SINT EUSTATIUS AND SABA", "BQ");
    europe.put("BOSNIA AND HERZEGOVINA", "BA");
    world1.put("BOTSWANA", "BW");
    world1.put("BOUVET ISLAND", "BV");
    world1.put("BRAZIL", "BR");
    world2.put("BRITISH INDIAN OCEAN TERRITORY", "IO");
    world1.put("BRUNEI DARUSSALAM", "BN");
    europe.put("BULGARIA", "BG");
    world1.put("BURKINA FASO", "BF");
    world1.put("BURUNDI", "BI");
    world1.put("CAMBODIA", "KH");
    world1.put("CAMEROON", "CM");
    world1.put("CANADA", "CA");
    world1.put("CAPE VERDE", "CV");
    world1.put("CAYMAN ISLANDS", "KY");
    world1.put("CENTRAL AFRICAN REPUBLIC", "CF");
    world1.put("CHAD", "TD");
    world1.put("CHILE", "CL");
    world2.put("CHINA", "CN");
    world2.put("CHRISTMAS ISLAND", "CX");
    world2.put("COCOS (KEELING) ISLANDS", "CC");
    world1.put("COLOMBIA", "CO");
    world1.put("COMOROS", "KM");
    world1.put("CONGO", "CG");
    world1.put("CONGO, THE DEMOCRATIC REPUBLIC OF THE", "CD");
    world2.put("COOK ISLANDS", "CK");
    world1.put("COSTA RICA", "CR");
    world1.put("CÔTE D'IVOIRE", "CI");
    europe.put("CROATIA", "HR");
    world1.put("CUBA", "CU");
    world1.put("CURAÇAO", "CW");
    europe.put("CYPRUS", "CY");
    europe.put("CZECH REPUBLIC", "CZ");
    europe.put("DENMARK", "DK");
    world1.put("DJIBOUTI", "DJ");
    world1.put("DOMINICA", "DM");
    world1.put("DOMINICAN REPUBLIC", "DO");
    world1.put("ECUADOR", "EC");
    world1.put("EGYPT", "EG");
    world1.put("EL SALVADOR", "SV");
    world1.put("EQUATORIAL GUINEA", "GQ");
    world1.put("ERITREA", "ER");
    europe.put("ESTONIA", "EE");
    world1.put("ETHIOPIA", "ET");
    world1.put("FALKLAND ISLANDS (MALVINAS)", "FK");
    europe.put("FAROE ISLANDS", "FO");
    world2.put("FIJI", "FJ");
    europe.put("FINLAND", "FI");
    europe.put("FRANCE", "FR");
    world1.put("FRENCH GUIANA", "GF");
    world2.put("FRENCH POLYNESIA", "PF");
    world2.put("FRENCH SOUTHERN TERRITORIES", "TF");
    world1.put("GABON", "GA");
    world1.put("GAMBIA", "GM");
    europe.put("GEORGIA", "GE");
    europe.put("GERMANY", "DE");
    world1.put("GHANA", "GH");
    europe.put("GIBRALTAR", "GI");
    europe.put("GREECE", "GR");
    europe.put("GREENLAND", "GL");
    world1.put("GRENADA", "GD");
    world1.put("GUADELOUPE", "GP");
    world2.put("GUAM", "GU");
    world1.put("GUATEMALA", "GT");
    europe.put("GUERNSEY", "GG");
    world1.put("GUINEA", "GN");
    world1.put("GUINEA-BISSAU", "GW");
    world1.put("GUYANA", "GY");
    world1.put("HAITI", "HT");
    world2.put("HEARD ISLAND AND MCDONALD ISLANDS", "HM");
    europe.put("HOLY SEE (VATICAN CITY STATE)", "VA");
    world1.put("HONDURAS", "HN");
    world1.put("HONG KONG", "HK");
    europe.put("HUNGARY", "HU");
    europe.put("ICELAND", "IS");
    world1.put("INDIA", "IN");
    world1.put("INDONESIA", "ID");
    world1.put("IRAN, ISLAMIC REPUBLIC OF", "IR");
    world1.put("IRAQ", "IQ");
    europe.put("IRELAND", "IE");
    uk.put("ISLE OF MAN", "IM");
    world1.put("ISRAEL", "IL");
    europe.put("ITALY", "IT");
    world1.put("JAMAICA", "JM");
    world2.put("JAPAN", "JP");
    europe.put("JERSEY", "JE");
    world1.put("JORDAN", "JO");
    europe.put("KAZAKHSTAN", "KZ");
    world1.put("KENYA", "KE");
    world2.put("KIRIBATI", "KI");
    world2.put("KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF", "KP");
    world2.put("KOREA, REPUBLIC OF", "KR");
    europe.put("KOSOVO", "XK");
    world1.put("KUWAIT", "KW");
    europe.put("KYRGYZSTAN", "KG");
    world2.put("LAO PEOPLE'S DEMOCRATIC REPUBLIC", "LA");
    europe.put("LATVIA", "LV");
    world1.put("LEBANON", "LB");
    world1.put("LESOTHO", "LS");
    world1.put("LIBERIA", "LR");
    world1.put("LIBYA", "LY");
    europe.put("LIECHTENSTEIN", "LI");
    europe.put("LITHUANIA", "LT");
    europe.put("LUXEMBOURG", "LU");
    world2.put("MACAO", "MO");
    europe.put("MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF", "MK");
    world1.put("MADAGASCAR", "MG");
    world1.put("MALAWI", "MW");
    world1.put("MALAYSIA", "MY");
    world1.put("MALDIVES", "MV");
    world1.put("MALI", "ML");
    europe.put("MALTA", "MT");
    world2.put("MARSHALL ISLANDS", "MH");
    world1.put("MARTINIQUE", "MQ");
    world1.put("MAURITANIA", "MR");
    world1.put("MAURITIUS", "MU");
    world1.put("MAYOTTE", "YT");
    world1.put("MEXICO", "MX");
    world2.put("MICRONESIA, FEDERATED STATES OF", "FM");
    europe.put("MOLDOVA, REPUBLIC OF", "MD");
    europe.put("MONACO", "MC");
    world2.put("MONGOLIA", "MN");
    europe.put("MONTENEGRO", "ME");
    world1.put("MONTSERRAT", "MS");
    world1.put("MOROCCO", "MA");
    world1.put("MOZAMBIQUE", "MZ");
    world1.put("MYANMAR", "MM");
    world1.put("NAMIBIA", "NA");
    world2.put("NAURU", "NR");
    world1.put("NEPAL", "NP");
    europe.put("NETHERLANDS", "NL");
    world2.put("NEW CALEDONIA", "NC");
    world2.put("NEW ZEALAND", "NZ");
    world1.put("NICARAGUA", "NI");
    world1.put("NIGER", "NE");
    world1.put("NIGERIA", "NG");
    world1.put("NIUE", "NU");
    world2.put("NORFOLK ISLAND", "NF");
    world2.put("NORTHERN MARIANA ISLANDS", "MP");
    europe.put("NORWAY", "NO");
    world1.put("OMAN", "OM");
    world1.put("PAKISTAN", "PK");
    world2.put("PALAU", "PW");
    world1.put("PALESTINE, STATE OF", "PS");
    world1.put("PANAMA", "PA");
    world2.put("PAPUA NEW GUINEA", "PG");
    world1.put("PARAGUAY", "PY");
    world1.put("PERU", "PE");
    world2.put("PHILIPPINES", "PH");
    world2.put("PITCAIRN", "PN");
    europe.put("POLAND", "PL");
    europe.put("PORTUGAL", "PT");
    world1.put("PUERTO RICO", "PR");
    world1.put("QATAR", "QA");
    world2.put("RÉUNION", "RE");
    europe.put("ROMANIA", "RO");
    europe.put("RUSSIAN FEDERATION", "RU");
    world1.put("RWANDA", "RW");
    world1.put("SAINT BARTHÉLEMY", "BL");
    world1.put("SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA", "SH");
    world1.put("SAINT KITTS AND NEVIS", "KN");
    world1.put("SAINT LUCIA", "LC");
    world1.put("SAINT MARTIN (FRENCH PART)", "MF");
    world1.put("SAINT PIERRE AND MIQUELON", "PM");
    world1.put("SAINT VINCENT AND THE GRENADINES", "VC");
    world2.put("SAMOA", "WS");
    europe.put("SAN MARINO", "SM");
    world1.put("SAO TOME AND PRINCIPE", "ST");
    world1.put("SAUDI ARABIA", "SA");
    world1.put("SENEGAL", "SN");
    europe.put("SERBIA", "RS");
    world1.put("SEYCHELLES", "SC");
    world1.put("SIERRA LEONE", "SL");
    world2.put("SINGAPORE", "SG");
    world1.put("SINT MAARTEN (DUTCH PART)", "SX");
    europe.put("SLOVAKIA", "SK");
    europe.put("SLOVENIA", "SI");
    world2.put("SOLOMON ISLANDS", "SB");
    world1.put("SOMALIA", "SO");
    world1.put("SOUTH AFRICA", "ZA");
    world2.put("SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS", "GS");
    world1.put("SOUTH SUDAN", "SS");
    europe.put("SPAIN", "ES");
    world1.put("SRI LANKA", "LK");
    world1.put("SUDAN", "SD");
    world1.put("SURINAME", "SR");
    europe.put("SVALBARD AND JAN MAYEN", "SJ");
    world1.put("SWAZILAND", "SZ");
    europe.put("SWEDEN", "SE");
    europe.put("SWITZERLAND", "CH");
    world1.put("SYRIAN ARAB REPUBLIC", "SY");
    world2.put("TAIWAN, PROVINCE OF CHINA", "TW");
    europe.put("TAJIKISTAN", "TJ");
    world1.put("TANZANIA, UNITED REPUBLIC OF", "TZ");
    world1.put("THAILAND", "TH");
    world2.put("TIMOR-LESTE", "TL");
    world1.put("TOGO", "TG");
    world2.put("TOKELAU", "TK");
    world2.put("TONGA", "TO");
    world1.put("TRINIDAD AND TOBAGO", "TT");
    world1.put("TUNISIA", "TN");
    europe.put("TURKEY", "TR");
    europe.put("TURKMENISTAN", "TM");
    world1.put("TURKS AND CAICOS ISLANDS", "TC");
    world2.put("TUVALU", "TV");
    world1.put("UGANDA", "UG");
    europe.put("UKRAINE", "UA");
    world1.put("UNITED ARAB EMIRATES", "AE");
    uk.put("UNITED KINGDOM", "GB");
    world1.put("UNITED STATES", "US");
    world1.put("UNITED STATES MINOR OUTLYING ISLANDS", "UM");
    world1.put("URUGUAY", "UY");
    europe.put("UZBEKISTAN", "UZ");
    world2.put("VANUATU", "VU");
    world1.put("VENEZUELA, BOLIVARIAN REPUBLIC OF", "VE");
    world1.put("VIET NAM", "VN");
    world2.put("VIRGIN ISLANDS, BRITISH", "VG");
    world2.put("VIRGIN ISLANDS, U.S.", "VI");
    world2.put("WALLIS AND FUTUNA", "WF");
    world1.put("WESTERN SAHARA", "EH");
    world1.put("YEMEN", "YE");
    world1.put("ZAMBIA", "ZM");
    world1.put("ZIMBABWE", "ZW");
  }

  /**
   * This is the ISO 3166 list of countries.
   * 
   * @return an array of countries suitable for UI drop down lists
   * @see http://www.iso.org/iso/country_codes
   */
  public static String[] generateCountriesList() {
    final List<String> countries = new ArrayList<String>(uk.size() + europe.size() + world1.size() + world2.size());
    countries.addAll(uk.keySet());
    countries.addAll(europe.keySet());
    countries.addAll(world1.keySet());
    countries.addAll(world2.keySet());
    Collections.sort(countries);
    return countries.toArray(new String[0]);
  }

  /**
   * Return the ISO 3166 country code for the given country.
   * 
   * @param country
   *          a country from the ISO 3166 list of countries
   * @return a two letter country code or the empty string if the country was not found
   */
  public static String findCountryCode(final String country) {
    if (BeanUtils.uk.get(country) != null) {
      return BeanUtils.uk.get(country);
    }
    if (BeanUtils.europe.get(country) != null) {
      return BeanUtils.europe.get(country);
    }
    if (BeanUtils.world1.get(country) != null) {
      return BeanUtils.world1.get(country);
    }
    if (BeanUtils.world2.get(country) != null) {
      return BeanUtils.world2.get(country);
    }
    return "";
  }

  /**
   * Calculate which Royal Mail shipping category a country is.
   * 
   * @param country
   *          a country from the ISO 3166 list of countries
   * @return a shipping category
   */
  public static Shipping calculateShippingCategory(final String country) {
    for (String match : BeanUtils.uk.keySet()) {
      if (match.equals(country)) {
        return Shipping.FIRST_UK;
      }
    }
    for (String match : BeanUtils.europe.keySet()) {
      if (match.equals(country)) {
        return Shipping.AIR_EUROPE;
      }
    }
    for (String match : BeanUtils.world2.keySet()) {
      if (match.equals(country)) {
        return Shipping.AIR_WORLD_2;
      }
    }
    return Shipping.AIR_WORLD_1;
  }
}
