package de.dbis.acis.cloud.Tethys.util;

import java.io.IOException;

import javax.servlet.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class provides additional Headers to each response made by Tethys to enable CORS (Cross Origin Resource Sharing).
 *
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
public class CORSFilter implements Filter {

  /**
   * Contains all allowed HTTP methods
   */
  public static String VALID_METHODS = "DELETE, HEAD, GET, OPTIONS, POST, PUT";

  /**
   * Adds the Headers
   */
  public void doFilter(
		ServletRequest request, ServletResponse response,
		FilterChain chain) throws IOException, ServletException {

		((HttpServletResponse)response).addHeader("Access-Control-Allow-Origin", "*");
		((HttpServletResponse)response).addHeader("Access-Control-Allow-Methods", VALID_METHODS);
		((HttpServletResponse)response).addHeader("Access-Control-Allow-Headers", "Accept, Content-Type, Origin, X-Auth-Token");

		chain.doFilter(request, response);
	}

  /**
   * Empty but needed
   */
  public void init(FilterConfig config) throws ServletException {
    // nothing to do here
  }

  /**
   * Empty but needed
   */
  public void destroy() {
    // nothing to do here
  }

}
