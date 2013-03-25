/**
 * Copyright 2013 Mat Booth <mbooth@apache.org>
 */
package com.gpm;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gpm.mbean.site.LogInBean;

/**
 * Filter to determine whether users are authenticated when trying to access secure URIs
 * and redirect them to a login page if not.
 * 
 * @author mbooth
 */
@WebFilter(filterName = "Log in Filter", value = { LogInFilter.SECURE_PATH + "*" })
public class LogInFilter implements Filter {

  public static final String SECURE_PATH = "/secure/";

  // Special paths to be treated differently
  private static final String HOME = "/index.xhtml";
  private static final String LOGIN = SECURE_PATH + "login.xhtml";
  private static final String REGISTER = SECURE_PATH + "register.xhtml";
  private static final String RECOVER = SECURE_PATH + "recover.xhtml";

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
      LogInBean logIn = (LogInBean) req.getSession().getAttribute("logInBean");
      if (logIn != null && logIn.isLoggedIn()) {
        // We are logged in already
        if (uri.startsWith(LOGIN) || uri.startsWith(REGISTER) || uri.startsWith(RECOVER)) {
          // Do not permit log into, register or recover accounts
          res.sendRedirect(HOME);
        } else {
          // Everything else is fine
          chain.doFilter(request, res);
        }
      } else {
        // We are not logged in
        if (uri.startsWith(LOGIN) || uri.startsWith(REGISTER) || uri.startsWith(RECOVER)) {
          // Let users log into, register or recover accounts
          chain.doFilter(request, res);
        } else {
          // Everything else must go through the log in page
          res.sendRedirect(LOGIN);
        }
      }
    } else {
      chain.doFilter(request, res);
    }
  }
}
