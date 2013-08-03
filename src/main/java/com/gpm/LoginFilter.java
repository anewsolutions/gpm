/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gpm.manager.UserAccountManager;
import com.gpm.manager.exception.UserAccountException;
import com.gpm.mbean.site.LoginBean;
import com.gpm.model.UserAccount;

/**
 * Filter to determine whether users are authenticated when trying to access secure URIs
 * and redirect them to a login page if not.
 * 
 * @author mbooth
 */
@WebFilter(filterName = "Log in Filter", value = { LoginFilter.SECURE_PATH + "*" })
public class LoginFilter implements Filter {

  // Paths controlled by authentication/authorisation
  public static final String SECURE_PATH = "/secure/";
  public static final String ADMIN_PATH = SECURE_PATH + "admin/";

  // Special paths to be treated differently
  private static final String HOME = "/index.xhtml";
  private static final String LOGIN = SECURE_PATH + "login.xhtml";
  private static final String REGISTER = SECURE_PATH + "register.xhtml";
  private static final String RECOVER = SECURE_PATH + "recover.xhtml";
  private static final String RESET = SECURE_PATH + "reset.xhtml";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String uri = req.getRequestURI();
    if (uri.startsWith(SECURE_PATH)) {
      LoginBean login = (LoginBean) req.getSession().getAttribute("loginBean");
      if (login == null) {
        login = new LoginBean();
        req.getSession().setAttribute("loginBean", login);
      }
      if (login.isLoggedIn()) {
        // We are logged in already
        if (uri.startsWith(LOGIN) || uri.startsWith(REGISTER) || uri.startsWith(RECOVER) || uri.startsWith(RESET)) {
          // Do not permit log into, register, recover or reset
          res.sendRedirect(HOME);
        } else if (uri.startsWith(ADMIN_PATH)) {
          // If an admin page is requested, check if user is an administrator
          UserAccount user = null;
          try {
            user = UserAccountManager.findByUuid(login.getUserUuid().toString());
          } catch (UserAccountException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          if (user != null && user.isAdministrator()) {
            // User is an administrator, permit them to continue
            chain.doFilter(request, res);
          } else {
            // User is not an administrator
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorised to view this page");
          }
        } else {
          // Everything else is fine
          chain.doFilter(request, res);
        }
      } else {
        // We are not logged in
        if (uri.startsWith(LOGIN) || uri.startsWith(REGISTER) || uri.startsWith(RECOVER) || uri.startsWith(RESET)) {
          // These pages are fine, so store the referring URI and let users through to the
          // log in, register or recover pages
          if (login.getRedirect() == null) {
            String referer = req.getHeader("referer");
            if (referer == null) {
              // If we cannot determine referer then use HOME as the referer
              referer = HOME;
            }
            try {
              login.setRedirect(new URI(referer));
            } catch (URISyntaxException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
          chain.doFilter(request, res);
        } else {
          // Everything else must go through the log in page, so store the original URI
          // and redirect to the log in page
          try {
            login.setRedirect(new URI(uri));
          } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          res.sendRedirect(LOGIN);
        }
      }
    } else {
      chain.doFilter(request, res);
    }
  }
}
