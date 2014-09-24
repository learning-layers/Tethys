package de.dbis.acis.cloud.i5cloud.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.dbis.acis.cloud.i5cloud.client.OpenstackClient;
import de.dbis.acis.cloud.i5cloud.entity.Services;
import de.dbis.acis.cloud.i5cloud.message.server.SMessageAuth;


/**
 * This SubResource matches the URL /Tethys/users/{iss}/{sub}
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de> *
 */
@Path("/users/{iss}/{sub}")
@Api(value="/users/{iss}/{sub}", description = "Operations about Files & Container")
public class UserStorageResource {

	@PathParam("iss") String iss;
	@PathParam("sub") String sub;
	
	@POST	
	@Consumes( { MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED } )
	@ApiOperation(value="Returns the details of a service.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response createStorage(){
		
		return null;
	}
	
	@GET
	@Path("/storage/{path:.+}")
	@ApiOperation(value="Get a list of all files in a sub-storage or a file with specified path")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response getFiles(@PathParam("path") String path) throws ClassNotFoundException, IOException{
		
		
//		CHECK IF ISS/SUB is right ELSE NO ACCESS
		SMessageAuth smessage = new SMessageAuth();
		smessage.setPassword("swift");
		smessage.setService("service");
		smessage.setUsername("swift");
		JsonObject key = OpenstackClient.adminAuth(smessage);
		
		if(key != null) {

			return Response.ok( OpenstackClient.getFile2(key.get("X-Auth-Token").getAsString(), key.get("service-id").getAsString(), iss+"."+sub+"/"+path) ).build();

		}
		return null;
	}
	
	@PUT
	@Path("/storage/{path : .+}")
	@Consumes( { MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED } )
	@ApiOperation(value="puts file/substorage with specified pat")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response putFile(@PathParam("path") String path, InputStream is) throws IOException {
		
		Response.ResponseBuilder r = null;
		
//		CHECK IF ISS/SUB is right ELSE NO ACCESS
		SMessageAuth smessage = new SMessageAuth();
		smessage.setPassword("swift");
		smessage.setService("service");
		smessage.setUsername("swift");
		JsonObject key = OpenstackClient.adminAuth(smessage);	

		if(key != null) {
			r =  OpenstackClient.uploadFile(is, key.get("X-Auth-Token").getAsString(), key.get("service-id").getAsString(), iss+"."+sub+"/"+path);
		}
		
		return r.build();
		
	}
	
	@GET
	@Path("/storage/")
	@ApiOperation(value="blah")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response test(){
		
		Response.ResponseBuilder r = null;
		JsonArray output = null;
		
//		CHECK IF ISS/SUB is right ELSE NO ACCESS
		SMessageAuth smessage = new SMessageAuth();
		smessage.setPassword("swift");
		smessage.setService("service");
		smessage.setUsername("swift");
		JsonObject key = OpenstackClient.adminAuth(smessage);	

		if(key != null) {
			output =  OpenstackClient.getUploadedFiles(key.get("X-Auth-Token").getAsString(), key.get("service-id").getAsString(), iss+"."+sub);
		}
		
		r = Response.ok(output).status(Status.OK);
		return r.build();
	}
}
