package de.dbis.acis.cloud.Tethys;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;

//@WebServlet(name = "SwaggerJaxrsServlet", loadOnStartup = 0)
//public class SwaggerJaxrsServlet extends HttpServlet {
@WebListener
public class SwaggerJaxrsListener implements ServletContextListener {

//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	@Override
//	public void init() {
//		System.out.println("Swagger starts...");
//		try{
////			SwaggerConfig swaggerConfig = new SwaggerConfig();
////			ConfigFactory.setConfig(swaggerConfig);
////			swaggerConfig.setBasePath("http://localhost:8081/Chocoloper2Tethys");
////			swaggerConfig.setApiVersion("1.0.0");
////			ScannerFactory.setScanner(new DefaultJaxrsScanner());
////			ClassReaders.setReader(new DefaultJaxrsApiReader());
//		} catch(Exception e) {
//			System.out.println("Fehler im Swag");
//		}
//		System.out.println("Swagger started!");
//		System.out.println("");
//	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Swagger starts...");
		SwaggerConfig swaggerConfig = new SwaggerConfig();
		ConfigFactory.setConfig(swaggerConfig);
		swaggerConfig.setBasePath("http://localhost:8081/Chocoloper2Tethys");
		swaggerConfig.setApiVersion("1.0.0");
		ScannerFactory.setScanner(new DefaultJaxrsScanner());
		ClassReaders.setReader(new DefaultJaxrsApiReader());
		System.out.println("Swagger started!");
		System.out.println("");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Swagger destroyed");
	}

}
