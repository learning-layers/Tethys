package de.dbis.acis.cloud.Tethys.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.dbis.acis.cloud.Tethys.client.OpenstackClient;
import de.dbis.acis.cloud.Tethys.message.server.SMessageAuth;

/**
 * This SubResource matches the URL /i5Cloud/auth
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Path("/auth")
@Api(value="/auth", description = "Operations about auth", position = 4)
public class AuthResource {	
	

	/**
	 * Used to get an X-Auth-Token - deprecated.
	 * <p>
	 * example:		curl -X POST http://137.226.58.11:8081/i5Cloud/auth -H 'Content-Type: application/json' -d '{"service":"", "username":"", "password":""}'
	 * <p>
	 * input		{"service":"", "username":"", "password":""}
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
	 * @deprecated
	 * @see ServiceResource#auth(int, JsonObject)
	 * @param userdata	the Json-Object in the Request-Body
	 * @return a Json
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Returns an array of all registered services.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "NOT FOUND")
	} )
	public Response adminauth(JsonObject userdata) {
		Gson gson = new Gson();
		SMessageAuth message = gson.fromJson(userdata, SMessageAuth.class);
		// auth against openstack and give back results which are needed for swift
		JsonObject output = new JsonObject();
		Response.ResponseBuilder r = null;
	
		if( message != null && message.getUsername() != null && message.getPassword() != null ) { 
			output = OpenstackClient.authOpenstack(message, true);
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


