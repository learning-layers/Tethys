package de.dbis.acis.cloud.Tethys.util;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;


/**
 * This class's purpose is to prevent a memory leak, because tomcat can't stop them jdbc-thread(mysqlconnection).
 *
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@WebListener // register it as you wish
public class ContainerContextClosedHandler implements ServletContextListener {

	/**
	 * Empty but needed
	 */
  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    // nothing to do here
  }

  /**
   * Clean up on Destroy
   */
  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    Enumeration<Driver> drivers = DriverManager.getDrivers();

    Driver driver = null;

    // clear drivers
    while(drivers.hasMoreElements()) {
      try {
        driver = drivers.nextElement();
        DriverManager.deregisterDriver(driver);
      } catch (SQLException ex) {
        // deregistration failed, might want to do something, log at the very least
        }
    }

    // MySQL driver leaves around a thread. This static method cleans it up.
    try {
      AbandonedConnectionCleanupThread.shutdown();
    } catch (InterruptedException e) {
      // again failure, not much you can do
    }
  }

}
