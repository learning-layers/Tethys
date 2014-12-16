package de.dbis.acis.cloud.Tethys;

import java.util.Map;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;


@ApplicationPath("/")
public class TethysApplication extends ResourceConfig {

	public TethysApplication() {
		packages("de.dbis.acis.cloud.Tethys.resource");
		register(new TethysBinder());
		register(new LoggingFilter());
//		property(ServerProperties.TRACING, "ALL");
		System.out.println("LOGBUCH EINTRAG 1: Tethys started!");
	}

}
