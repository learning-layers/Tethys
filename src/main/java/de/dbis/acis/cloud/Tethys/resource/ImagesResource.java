package de.dbis.acis.cloud.Tethys.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.dbis.acis.cloud.Tethys.client.OpenstackClient;
import de.dbis.acis.cloud.Tethys.entity.Services;
import de.dbis.acis.cloud.Tethys.jpa.ServicesJpa;
import de.dbis.acis.cloud.Tethys.jpa.impl.ServicesJpaImpl;

@Path("/services/{service-id}/images")
@Api(value="/services/{service-id}/images", description = "Operations about images")
public class ImagesResource {

	private ServicesJpa servicesService;
	
	public ImagesResource() {
		servicesService = new ServicesJpaImpl();
	}
	
	
	@GET
	@Produces( { MediaType.APPLICATION_JSON } )
	@ApiOperation(value="Returns all Images & Snapshots.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response getImages(@PathParam("service-id") int sid,@HeaderParam("X-Auth-Token") String xAuthToken) {
		
		JsonObject output = null;
		Services service = servicesService.getServiceById(sid);
		
		if( service==null ) { 
			return Response.ok().status(Status.NOT_FOUND).build();
		}		
		
		output = OpenstackClient.getImages(xAuthToken, service.getTenantID());
		
		return Response.ok(output).status(Status.OK).build();
	}
	
}
