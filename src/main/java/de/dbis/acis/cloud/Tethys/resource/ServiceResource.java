package de.dbis.acis.cloud.Tethys.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.dbis.acis.cloud.Tethys.client.OpenstackClient;
import de.dbis.acis.cloud.Tethys.entity.Services;
import de.dbis.acis.cloud.Tethys.jpa.ServicesJpa;
import de.dbis.acis.cloud.Tethys.jpa.impl.ServicesJpaImpl;
import de.dbis.acis.cloud.Tethys.message.server.SMessageAuth;
import de.dbis.acis.cloud.Tethys.util.GsonExclusionStrategy;

/**
 * This SubResource matches the URL /i5Cloud/services/{service-id}
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de> *
 */
@Path("/services/{service-id}")
@Api("")
public class ServiceResource {

	private ServicesJpa servicesService;
	//private Gson gson = new Gson();
	private Gson gsonExternal = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();
	
	/**
	 * Class Constructor
	 */
	public ServiceResource() {
		servicesService = new ServicesJpaImpl();
	}

    
	/**
	 * A link to the SubResource ServiceFlavorsResource.class
	 * 
	 * @see AuthveResource
	 * @param rc the context
	 * @return the ServiceFlavorsResource.class
	 */
    @Path("/serviceflavors")
    public Object getServiceFlavors(@Context ResourceContext rc) {
        	return rc.getResource(ServiceFlavorsResource.class);
    }


    /**
     * Get the Details of a service. 
     * <p>
     * example:		curl -X GET http://localhost:8081/i5Cloud/services/{service-id}
     * <p>	
	 * response:	{"X-Auth-Token":"","expires":"","service-id":"","swift-url":""}
	 * <p>
	 * Responses:
	 * <ul>
	 *		<li>200 OK - everything worked</li>
	 *		<li>407 Not Found - auth data can't be found in openstack</li>
     * <ul>
     * 
     * @param sid	the service-id in the URL
     * @return a Json conntaining the details of a service.
     */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Returns the details of a service.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response getServiceDetails(@PathParam("service-id") int sid) {
		Services service = servicesService.getServiceById(sid);
		Response.ResponseBuilder r = null;
		if(service==null) { r = Response.ok(service).status(Status.NOT_FOUND); }
		else { r = Response.ok(gsonExternal.toJsonTree(service)).status(Status.OK); }
		return r.build();
	}

	/**
	 * Returns the limits of a service defined by openstack.
	 * <p>
	 * example: curl -X GET http://137.226.58.11:8081/i5Cloud/services/{service-id}/limits -H "X-Auth-Token:"
	 * <p>
	 * header:		X-Auth-Token
	 * <p>
	 * response:	really big...
	 * <p>
	 * Responses:
	 * <ul>
	 *		<li>200 OK - everything worked</li>
	 *		<li>406 Not Acceptable - the json formation is wrong</li>
	 *		<li>407 Not Found - wrong data; can't be found in openstack</li> 
	 * </ul>
	 * 
	 * @param sid			the service-id in the URL
	 * @param xAuthToken	the X-Auth-Token from the Header
	 * @return a Json containing the limits of a service defined by openstack  
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/limits")
	@ApiOperation(value="Returns an array of all registered services.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
    public Response getServiceLimits(@PathParam("service-id") int sid, @HeaderParam("X-Auth-Token") String xAuthToken) {
		JsonObject output = new JsonObject();
		Response.ResponseBuilder r = null;
		Services service = servicesService.getServiceById(sid);
	
		if( service==null || xAuthToken==null) { 
			r = Response.ok().status(Status.NOT_ACCEPTABLE);
			return r.build();
		}
		
		output = OpenstackClient.serviceLimits(xAuthToken,service.getTenantID());
		if(output==null){ r = Response.ok(output).status(Status.NOT_FOUND);}
		else{ r = Response.ok(output).status(Status.OK);}
		
		return r.build();
		

    }
	

	/**
	 * Returns an X-Auth-Token.
	 * <p>
	 * example:		curl -X POST http://137.226.58.11:8081/i5Cloud/services/{service-id}/auth -H 'Content-Type: application/json' -d '{"username":"", "password":""}'
	 * <p>
	 * input		{"username":"", "password":""}
	 * <p>
	 * output:		{"X-Auth-Token":"","expires":"","tenant-id":"","swift-url":""}
	 * <p>
	 * Responses:
	 * <ul>
	 *		<li>200 OK - everything worked</li>
	 *		<li>406 Not Acceptable - the json formation is wrong</li>
	 *		<li>407 Not Found - auth data can't be found in openstack</li>
	 * <ul>
	 * 
	 * @param sid		the service-id in the URL
	 * @param userdata the Json-Object in the Request-Body
	 * @return a Json
	 */
	@POST
	@Path("/auth")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Returns an array of all registered services.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response auth(@PathParam("service-id") int sid, JsonObject userdata) {
		// auth against openstack and give back results which are needed for swift
		
		Gson gson = new Gson();
		SMessageAuth message = gson.fromJson(userdata, SMessageAuth.class);
		
		JsonObject output = new JsonObject();
		Response.ResponseBuilder r = null;
		Services service = null;
		service = servicesService.getServiceById(sid);
		
		message.setService(service.getName());
		
		if( userdata != null && service != null && userdata.has("username") && userdata.has("password") ) { 
			output = OpenstackClient.manipulateAuthAndReturnToken(message);
			if(output==null){ r = Response.ok(output).status(Status.NOT_FOUND);}
			else{ r = Response.ok(output).status(Status.OK);}

		}
		else {
			output.addProperty("error", "wrong json structure");
			r = Response.ok(output).status(Status.NOT_ACCEPTABLE);
		}
		return r.build();
	}
}