package de.dbis.acis.cloud.Tethys.resource;


import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
	/**
	 * @param xAuthToken
	 * @param userdata
	 * @return
	 */
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
   
	
	//curl -v -X GET http://137.226.58.11:8081/Tethys/users/openIDauth
	/**
	 * @return
	 */
	@Path("/openIDauth")
	@GET
	@ApiOperation(value="Redirects to openID Connect Login Page.")
	@ApiResponses( {
		@ApiResponse(code = 302, message = "FOUND"),
	} )
	public Response getOpenIDLoginPage() {
		Random rand = new Random();
		String openIDLoginPageURL =  "http://api.learning-layers.eu/o/oauth2/authorize";
		openIDLoginPageURL += "?response_type=code";
		openIDLoginPageURL += "&scope=openid";
		openIDLoginPageURL += "&client_id=0a8a1de7-1871-41bb-9801-f05acc07414c";
		long a;
		while((a = rand.nextLong())<=0){}
		openIDLoginPageURL += "&state="+a;
		openIDLoginPageURL += "&redirect_uri=http%3A%2F%2Fcloud11.dbis.rwth-aachen.de%3A8081%2Fusers%2FTODO";

		return Response.status(302).header("Location", openIDLoginPageURL).build();
	}
	
	/**
	 * @return
	 */
	@POST
	@Path("/users/{sub}")
	@Consumes( { MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED } )
	@ApiOperation(value="Returns the details of a service.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response createStorage(){
		
		return null;
	}
	
}