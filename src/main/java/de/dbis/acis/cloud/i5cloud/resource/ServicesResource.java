package de.dbis.acis.cloud.i5cloud.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.core.ResourceContext;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.dbis.acis.cloud.i5cloud.client.OpenstackClient;
import de.dbis.acis.cloud.i5cloud.entity.Services;
import de.dbis.acis.cloud.i5cloud.jpa.ServicesJpa;
import de.dbis.acis.cloud.i5cloud.jpa.impl.ServicesJpaImpl;
import de.dbis.acis.cloud.i5cloud.message.server.SMessageAuth;
import de.dbis.acis.cloud.i5cloud.util.GsonExclusionStrategy;

/**
 * This SubResource matches the URL /i5Cloud/services
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Path("/services")
@Api(value="/services", description = "Operations about services", position = 1)
public class ServicesResource {


	private ServicesJpa servicesService;
	private Gson gson = new Gson();
	private Gson gsonExternal = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();
	
	/**
	 * Class Constructor
	 */
	public ServicesResource() {
		servicesService = new ServicesJpaImpl();
	}
	
//	SWAGGER-BACKUP
//	/**
//	 * A link to the SubResource ServiceResource.class
//	 * 
//	 * @see ServiceResource
//	 * @param sid	the service-id in the url
//	 * @param rc	the context
//	 * @return the ServiceResource.class
//	 */
//    @Path("/{service-id}")
//    public ServiceResource getService(@Context @InjectParam("ServiceResource") ServiceResource sr) {
//    	return sr;
//    }
    
	/**
	 * A link to the SubResource ServiceResource.class
	 * 
	 * @see ServiceResource
	 * @param sid	the service-id in the url
	 * @param rc	the context
	 * @return the ServiceResource.class
	 */
    @Path("/{service-id}")
    public ServiceResource getServiceResource(@Context ResourceContext rc) {
        return rc.getResource(ServiceResource.class);
    }
    
    
    /**
     * Returns an array of all registered services.
     * <p>
     * example:		curl -X GET http://localhost:8081/i5Cloud/services
     * <p>
     * input:		none
     * <p> 		
	 * response:	[{"X-Auth-Token":"","expires":"","service-id":"","swift-url":""},...]
	 * <p>
	 * Responses:
	 * <ul>
	 *		<li>200 OK - everything worked</li>
	 *		<li>407 Not Found - auth data can't be found in openstack<li>
     * <ul>
     * 
     * @return a json-array of all services
     */	
	@GET
	@Produces(MediaType.APPLICATION_JSON )
	@ApiOperation(value="Returns an array of all registered services.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response getAllServices() {
		List<Services> listeServices = servicesService.getAllServices();
		return Response.ok(gsonExternal.toJsonTree(listeServices)).status(Status.OK).build();
	}
	
	/**
	 * Register your existing Openstack-service to i5Cloud.
	 * <p>
	 * curl -X POST http://137.226.58.11:8081/i5Cloud/services -H 'X-Auth-Token: ' -H 'Content-Type: application/json' -d '{"service":"TestService","description":"NEU"}'
	 * example:		curl -X POST http://137.226.58.11:8081/i5Cloud/services -H 'X-Auth-Token: <X-Auth-Token>' -H 'Content-Type: application/json' -d '{"service":"","description":""}'
	 * <p>
	 * input		{"service":"","description":""}
	 * <p>
	 * output:		{"service-id":"","name":"","description":"","creationtime":""}
	 * <p>
	 * Responses:
	 * <ul>
	 *		<li>200 OK - everything worked</li>
	 *		<li>406 Not Acceptable - the json formation is wrong</li>
	 *		<li>407 Not Found - auth data can't be found in openstack</li>
	 *		<li>409 Conflict - Already registered Service</li>
	 * </ul>
	 * 
	 * @param userdata the data of the user who want's to register his service
	 * @return whether the service could be registered or not as Json
	 */
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Returns an array of all registered services.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
    public Response createNewService(@HeaderParam("X-Auth-Token") String xAuthToken, JsonObject servicedata) {
		
		Services service = gson.fromJson(servicedata, Services.class);
		
		Response.ResponseBuilder r = null;
		JsonObject output = new JsonObject();
		JsonObject response = null;
		
		//test if wrong input
		if( service == null  ) { 
			output.addProperty("error", "wrong json structure");
			r = Response.ok(output).status(Status.NOT_ACCEPTABLE);
			return r.build();
		}
		
		response = OpenstackClient.createNewService(service.getName(),service.getDescription(), xAuthToken);
		if(response==null)
		{ 
			r = Response.ok(response).status(Status.NOT_FOUND);
			return r.build();
		}
		
		//Create json/service object
		output.add("name", response.getAsJsonObject("tenant").get("name"));
		output.add("tenantid", response.getAsJsonObject("tenant").get("id"));
		output.add("description", response.getAsJsonObject("tenant").get("description"));
		
		service = gson.fromJson(output,Services.class);
		
		//Create database entry
		if(servicesService.getServiceByName(service.getName()) != null) {
			r = Response.status(Status.CONFLICT);
			return r.build();
		}
		servicesService.save(service);
		
		//fetch the id and delete tenantid
		service = servicesService.getServiceByName(service.getName()); 
		output = gsonExternal.toJsonTree(service, Services.class).getAsJsonObject();
		
		return Response.ok(output).status(Status.CREATED).build();
		
	}
	
	
//	/**
//	 * Register your existing Openstack-service to i5Cloud.
//	 * <p>
//	 * example:		curl -X POST http://137.226.58.11:8081/i5Cloud/services -H 'Content-Type: application/json' -d '{"service":"", "username":"", "password":""}'
//	 * <p>
//	 * input		{"tenant":"", "username":"", "password":""}
//	 * <p>
//	 * output:		{"service-id":"","name":"","description":"","creationtime":""}
//	 * <p>
//	 * Responses:
//	 * <ul>
//	 *		<li>200 OK - everything worked</li>
//	 *		<li>406 Not Acceptable - the json formation is wrong</li>
//	 *		<li>407 Not Found - auth data can't be found in openstack</li>
//	 *		<li>409 Conflict - Already registered Service</li>
//	 * </ul>
//	 * 
//	 * @param userdata the data of the user who want's to register his service
//	 * @return whether the service could be registered or not as Json
//	 */
//	@POST
//    @Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("register")
//    public Response RegisterService(JsonObject userdata) {
//		
//		Gson gson = new Gson();
//		SMessageAuth smessage = gson.fromJson(userdata, SMessageAuth.class);
//		
//		JsonObject response = null;
//		JsonObject output = new JsonObject();
//		Services service = null;
//		Response.ResponseBuilder r = null;
//		
//		//test if wrong input
//		if( smessage == null || smessage.getUsername().isEmpty() || smessage.getService().isEmpty() || smessage.getPassword().isEmpty() ) { 
//			output.addProperty("error", "wrong json structure");
//			r = Response.ok(output).status(Status.NOT_ACCEPTABLE);
//			return ResponseCorsFilter.makeCORS(r, _corsHeaders);
//		}
//		
//		//auth/fetch service data
//		response = OpenstackClient.manipulateAuthAndReturnToken(smessage);
//		if(response==null)
//		{ 
//			r = Response.ok(response).status(Status.NOT_FOUND);
//			return ResponseCorsFilter.makeCORS(r, _corsHeaders);
//		}
//		
//		//Create json/service object
//		output.add("name", response.getAsJsonObject("access").getAsJsonObject("token").getAsJsonObject("tenant").get("name"));
//		output.add("tenantid", response.getAsJsonObject("access").getAsJsonObject("token").getAsJsonObject("tenant").get("id"));
//		output.add("description", response.getAsJsonObject("access").getAsJsonObject("token").getAsJsonObject("tenant").get("description"));
//		
//		service = gson.fromJson(output,Services.class);
//		
//		//Create database entry
//		if(servicesService.getServiceByName(service.getName()) != null) {
//			r = Response.status(Status.CONFLICT);
//			return ResponseCorsFilter.makeCORS(r, _corsHeaders);
//		}
//		servicesService.save(service);
//		
//		//fetch the id and delete tenantid
//		service = servicesService.getServiceByName(service.getName()); 
//		output = gsonExternal.toJsonTree(service, Services.class).getAsJsonObject();
//		
//		r = Response.ok(output).status(Status.CREATED);
//		return ResponseCorsFilter.makeCORS(r, _corsHeaders);
//    }
	
//	/**
//	 * Register your existing Openstack-service to i5Cloud.
//	 * <p>
//	 * example:		curl -X POST http://137.226.58.11:8081/i5Cloud/services -H 'Content-Type: application/json' -d '{"service":"", "username":"", "password":""}'
//	 * <p>
//	 * input		{"tenant":"", "username":"", "password":""}
//	 * <p>
//	 * output:		{"service-id":"","name":"","description":"","creationtime":""}
//	 * <p>
//	 * Responses:
//	 * <ul>
//	 *		<li>200 OK - everything worked</li>
//	 *		<li>406 Not Acceptable - the json formation is wrong</li>
//	 *		<li>407 Not Found - auth data can't be found in openstack</li>
//	 *		<li>409 Conflict - Already registered Service</li>
//	 * </ul>
//	 * 
//	 * @param userdata the data of the user who want's to register his service
//	 * @return whether the service could be registered or not as Json
//	 */
//	@POST
//    @Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//    public Response fetchAndRegisterService(JsonObject userdata) {
//		
//		Gson gson = new Gson();
//		SMessageAuth smessage = gson.fromJson(userdata, SMessageAuth.class);
//		
//		JsonObject response = null;
//		JsonObject output = new JsonObject();
//		Services service = null;
//		Response.ResponseBuilder r = null;
//		
//		//test if wrong input
//		if( smessage == null || smessage.getUsername().isEmpty() || smessage.getService().isEmpty() || smessage.getPassword().isEmpty() ) { 
//			output.addProperty("error", "wrong json structure");
//			r = Response.ok(output).status(Status.NOT_ACCEPTABLE);
//			return ResponseCorsFilter.makeCORS(r, _corsHeaders);
//		}
//		
//		//auth/fetch service data
//		response = OpenstackClient.manipulateAuthAndReturnToken(smessage);
//		if(response==null)
//		{ 
//			r = Response.ok(response).status(Status.NOT_FOUND);
//			return ResponseCorsFilter.makeCORS(r, _corsHeaders);
//		}
//		
//		//Create json/service object
//		output.add("name", response.getAsJsonObject("access").getAsJsonObject("token").getAsJsonObject("tenant").get("name"));
//		output.add("tenantid", response.getAsJsonObject("access").getAsJsonObject("token").getAsJsonObject("tenant").get("id"));
//		output.add("description", response.getAsJsonObject("access").getAsJsonObject("token").getAsJsonObject("tenant").get("description"));
//		
//		service = gson.fromJson(output,Services.class);
//		
//		//Create database entry
//		if(servicesService.getServiceByName(service.getName()) != null) {
//			r = Response.status(Status.CONFLICT);
//			return ResponseCorsFilter.makeCORS(r, _corsHeaders);
//		}
//		servicesService.save(service);
//		
//		//fetch the id and delete tenantid
//		service = servicesService.getServiceByName(service.getName()); 
//		output = gsonExternal.toJsonTree(service, Services.class).getAsJsonObject();
//		
//		r = Response.ok(output).status(Status.CREATED);
//		return ResponseCorsFilter.makeCORS(r, _corsHeaders);
//    }
// curl -i -X PUT -H "X-Auth-Token: 8ea828fb8c314011b4b7127a9b39d1d5" -H "X-Container-Read: .r:*,.rlistings"     http://137.226.58.2:8888/v1/AUTH_451035e5f9504a878946697522070c43/public/	

		
}
