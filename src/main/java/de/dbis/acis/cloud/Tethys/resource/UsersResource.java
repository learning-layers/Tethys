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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.ClientResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.dbis.acis.cloud.Tethys.client.OpenstackClient;
import de.dbis.acis.cloud.Tethys.util.GsonExclusionStrategy;

/**
 * This SubResource matches the URL /i5Cloud/users
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Path("/users")
@Api(value="/users", description = "Operations about users", position = 3)
public class UsersResource {

	private Gson gson = new Gson();
	private Gson gsonExternal = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();
	
	//curl -X POST http://137.226.58.11:8081/i5Cloud/users -H 'X-Auth-Token: 88a6923e7a8f45bab034a7aacd191120' -H 'Content-Type: application/json' -d '{"name":"1","password":"1"}'
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Returns an array of all registered services.")
	@ApiResponses( {
		@ApiResponse(code = 201, message = "CREATED"),
		@ApiResponse(code = 404, message = "NOT FOUND")
	} )
    public Response createNewUser(@HeaderParam("X-Auth-Token") String xAuthToken, JsonObject userdata) {
		
		JsonObject response;
		Response.ResponseBuilder r = null;
		
		response = OpenstackClient.createNewUser(xAuthToken, userdata.get("name").getAsString(), userdata.get("password").getAsString(), userdata.has("email")?userdata.get("email").getAsString():null, null, true);
		if(response==null)
		{ 
			r = Response.ok(response).status(Status.NOT_FOUND);
			return r.build();
		}
		
		return Response.ok(response).status(Status.CREATED).build();
		
	}
   
}