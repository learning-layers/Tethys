package de.dbis.acis.cloud.i5cloud.resource;


import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import de.dbis.acis.cloud.i5cloud.client.OpenstackClient;
import de.dbis.acis.cloud.i5cloud.entity.Services;
import de.dbis.acis.cloud.i5cloud.jpa.ServicesJpa;
import de.dbis.acis.cloud.i5cloud.jpa.impl.ServicesJpaImpl;
  
/**
 * This SubResource matches the URL /i5Cloud/services/{service-id}/serviceflavors
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de> *
 */
public class ServiceFlavorsResource {

	private ServicesJpa servicesService;
	
	/**
	 * Class Constructor
	 */
	public ServiceFlavorsResource() {
		servicesService = new ServicesJpaImpl();
	}
	
	
	/**
	 * TODO
	 * Need to be refactored
	 * 
	 * @param sid			the service-id in the URL
	 * @param path			the path behind "/data/" in the URL
	 * @param xAuthToken	the X-Auth-Token from the Header 
	 * @param is			the data as InputStream
	 * @return whether the data could be uploaded or not
	 * @throws IOException
	 */
	@PUT
	@Path("{path : .+}")
	public Response testUploadFile(@PathParam("service-id") int sid, @PathParam("path") String path, @HeaderParam("X-Auth-Token") String xAuthToken, InputStream is) throws IOException {

		Response.ResponseBuilder r = null;
		Services service = null;
		
		service = servicesService.getServiceById(sid);
		if(service != null) {
			r =  OpenstackClient.uploadFile(is, xAuthToken, service.getTenantID(), "serviceflavors/"+path);
		}
		
		return r.build();
		
	}
	
}
