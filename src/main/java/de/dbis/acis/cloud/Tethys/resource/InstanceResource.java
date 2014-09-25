package de.dbis.acis.cloud.Tethys.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

@Api("")
public class InstanceResource {

	private ServicesJpa servicesService;
	
	public InstanceResource() {
		servicesService = new ServicesJpaImpl();
	}
	
	@POST
	@Path("/start")
	@ApiOperation(value="Returns all Images & Snapshots.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response startInstance(@PathParam("service-id") int sid,@PathParam("instance-id") String iid,@HeaderParam("X-Auth-Token") String xAuthToken) {
		JsonObject action = new JsonObject();
		action.addProperty("os-start", "null");
		return doActionOnInstance(sid,iid,xAuthToken,action);
	}
	
	@POST
	@Path("/stop")
	@ApiOperation(value="Returns all Images & Snapshots.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response stopInstance(@PathParam("service-id") int sid,@PathParam("instance-id") String iid,@HeaderParam("X-Auth-Token") String xAuthToken) {
		JsonObject action = new JsonObject();
		action.addProperty("os-stop", "null");
		return doActionOnInstance(sid,iid,xAuthToken,action);
	}
	
	@POST
	@Path("/snapshot")
	@ApiOperation(value="{createImage: {name: ... , metadata: {}}}" )
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response startInstance(@PathParam("service-id") int sid,@PathParam("instance-id") String iid,@HeaderParam("X-Auth-Token") String xAuthToken, JsonObject json) {
		//'{"createImage": {"name": "test", "metadata": {}}}'
		return doActionOnInstance(sid,iid,xAuthToken,json);
	}
	
	public Response doActionOnInstance(int sid, String iid, String xAuthToken, JsonObject action) {
		
		JsonObject output = null;
		Services service = servicesService.getServiceById(sid);
		
		if( service==null ) { 
			return Response.ok().status(Status.NOT_FOUND).build();
		}		
		//TODO Check if instance exists
		
		output = OpenstackClient.doActionOnInstance(xAuthToken, service.getTenantID(), iid, action);
		
		return Response.ok(output).status(Status.ACCEPTED).build();
	}
}
