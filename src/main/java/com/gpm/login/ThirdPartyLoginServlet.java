/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm.login;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gpm.manager.ConfigurationManager;
import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.ConfigurationException;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.mbean.site.LoginBean;
import com.gpm.model.Configuration;

/**
 * This servlet receives redirected requests from third-party federated log in services.
 * 
 * @author mbooth
 */
@WebServlet(name = "Third Party Login Servlet", value = { ThirdPartyLoginServlet.THIRDPARTY_PATH + "*" })
public class ThirdPartyLoginServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private Log log = LogFactory.getLog(ThirdPartyLoginServlet.class);

  public static final String THIRDPARTY_PATH = "/thirdparty/";
  public static final String FACEBOOK_LOGIN = "facebook.login";
  public static final String GOOGLE_LOGIN = "google.login";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Delegate to POST handler
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Dispatch to third-party service specific handlers
    if (request.getPathInfo().endsWith(FACEBOOK_LOGIN)) {
      facebookLogin(request, response);
    } else if (request.getPathInfo().endsWith(GOOGLE_LOGIN)) {
      googleLogin(request, response);
    } else {
      log.error("Third party log in service not found");
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Handle Facebook's federated log in service requests.
   */
  private void facebookLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException,
      IOException {
    String code = request.getParameter("code");
    String state = request.getParameter("state");
    // Check that session ID matches what was sent to Facebook
    if (request.getSession().getId().equals(state)) {
      // Check that Facebook sent us a code back
      if (code != null && !code.isEmpty()) {
        String token = getFacebookToken(request, code);
        Map<String, String> details = getFacebookDetails(token);
        String ident = details.get("id");
        try {
          // Log in and redirect to the original page the user wanted
          UserAccountManager.createNewFacebook(details.get("email"), details.get("name"), ident, token);
          LoginBean logIn = (LoginBean) request.getSession().getAttribute("loginBean");
          logIn.loginFacebook(ident);
          response.sendRedirect(logIn.getRedirect().toString());
        } catch (UserAccountException e) {
          // Unable to talk to database
          log.error("Unable to retrieve or create facebook user account: " + e.getMessage());
          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
      } else {
        // Login failed or user declined authorisation
        log.error("Login failed or user declined authorisation, cannot log in");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      }
    } else {
      // Session ID mis-match
      log.error("Session ID mis-match, cannot log in");
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Handle Google's federated log in service requests.
   */
  private void googleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException,
      IOException {

  }

  public static String getFacebookLoginUrl(HttpServletRequest request) {
    String redirect = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
        + THIRDPARTY_PATH + FACEBOOK_LOGIN;
    String state = request.getSession(false).getId();
    String key = null;
    try {
      Properties props = ConfigurationManager.findPropsByKey(FACEBOOK_LOGIN);
      key = props.getProperty(FACEBOOK_LOGIN + ".key");
      if (key == null || key.isEmpty()) {
        return null;
      }
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
    String url = "https://www.facebook.com/dialog/oauth?client_id=" + key + "&redirect_uri=" + redirect
        + "&scope=email&state=" + state;
    return url;
  }

  public static String getGoogleLoginUrl(HttpServletRequest request) {
    String redirect = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
        + THIRDPARTY_PATH + GOOGLE_LOGIN;
    String state = request.getSession(false).getId();
    String key = null;
    try {
      Properties props = ConfigurationManager.findPropsByKey(GOOGLE_LOGIN);
      key = props.getProperty(GOOGLE_LOGIN + ".key");
      if (key == null || key.isEmpty()) {
        return null;
      }
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
    String url = "https://accounts.google.com/o/oauth2/auth?client_id=" + key + "&redirect_uri=" + redirect
        + "&response_type=code&scope=openid%20email&state=" + state;
    return url;
  }

  private static String getFacebookToken(HttpServletRequest request, String code) {
    String redirect = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
        + THIRDPARTY_PATH + FACEBOOK_LOGIN;
    String key = "";
    try {
      Configuration config = ConfigurationManager.findByKey("facebook.key");
      key = config.getConfigValue();
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String secret = "";
    try {
      Configuration config = ConfigurationManager.findByKey("facebook.secret");
      secret = config.getConfigValue();
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String url = "https://graph.facebook.com/oauth/access_token?client_id=" + key + "&redirect_uri=" + redirect
        + "&client_secret=" + secret + "&code=" + code;
    // Fetch and parse token
    HttpClient httpclient = new DefaultHttpClient();
    HttpGet httpget = new HttpGet(url);
    try {
      HttpResponse httpresponse = httpclient.execute(httpget);
      String entity = EntityUtils.toString(httpresponse.getEntity());
      String attributes[] = entity.split("&");
      for (String attribute : attributes) {
        String parts[] = attribute.split("=");
        if ("access_token".equalsIgnoreCase(parts[0])) {
          return parts[1];
        }
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      httpget.releaseConnection();
      httpclient.getConnectionManager().shutdown();
    }
    return null;
  }

  private static Map<String, String> getFacebookDetails(String token) {
    String url = "https://graph.facebook.com/me?access_token=" + token + "&fields=id,name,email";
    // Fetch and parse details
    HttpClient httpclient = new DefaultHttpClient();
    HttpGet httpget = new HttpGet(url);
    InputStreamReader in = null;
    try {
      HttpResponse httpresponse = httpclient.execute(httpget);
      Type map = new TypeToken<Map<String, String>>() {
      }.getType();
      in = new InputStreamReader(httpresponse.getEntity().getContent());
      return new Gson().fromJson(in, map);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      httpget.releaseConnection();
      httpclient.getConnectionManager().shutdown();
    }
    return null;
  }
}
