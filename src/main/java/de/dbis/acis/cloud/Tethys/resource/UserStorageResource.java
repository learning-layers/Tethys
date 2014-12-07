package de.dbis.acis.cloud.Tethys.resource;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.dbis.acis.cloud.Tethys.client.OpenstackClient;
import de.dbis.acis.cloud.Tethys.entity.LDAP.LDAPUserInfo;
import de.dbis.acis.cloud.Tethys.message.server.SMessageAuth;


/**
 * This Resource matches the URL /Tethys/users/{sub}
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de> *
 */
@Path("/users/storage")
@Api(value="/users/storage", description = "User storage ")
public class UserStorageResource {
	

	/**
	 * @param path
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GET
	@Path("/{path:.+}")
	@ApiOperation(value="Get a list of all files in a sub-storage or a file with specified path")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response getFiles(@HeaderParam("Authorization") String accessToken, @PathParam("path") String path) throws ClassNotFoundException, IOException{
		
		Response r = null;

		if(accessToken == null || accessToken.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		LDAPUserInfo ldapUser = OpenstackClient.verifyAccessToken(accessToken);
		
		JsonObject key = OpenstackClient.adminAuth(this.getSwiftCredentials());		
		
//		LOGG
		this.logKeystoneAuthResponse(key);
		
		if(key != null) {
				try {
					r = OpenstackClient.getFile2(key.get("X-Auth-Token").getAsString(), key.get("tenant-id").getAsString(), ldapUser.getPreferred_username()+"/"+path);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return r;
	}
	
	/**
	 * @param path
	 * @param is
	 * @return
	 * @throws IOException
	 */
	@PUT
	@Path("/{path : .+}")
	@Consumes( { MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED } )
	@ApiOperation(value="puts file/substorage with specified pat")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response putFile(@HeaderParam("Authorization") String accessToken, @PathParam("path") String path, InputStream is) throws IOException {
		
		Response.ResponseBuilder r = null;
		
		if(accessToken == null || accessToken.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		LDAPUserInfo ldapUser = OpenstackClient.verifyAccessToken(accessToken);
		
		JsonObject key = OpenstackClient.adminAuth(this.getSwiftCredentials());	
		
//		LOGG
		this.logKeystoneAuthResponse(key);

		if(key != null) {
			r =  OpenstackClient.uploadFile(is, key.get("X-Auth-Token").getAsString(), key.get("tenant-id").getAsString(), ldapUser.getPreferred_username()+"/"+path);
		}
		
		return r.build();
		
	}
	
	/**
	 * @return
	 */
	@GET
	@ApiOperation(value="blah")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response test(@HeaderParam("Authorization") String accessToken){
		
		Response.ResponseBuilder r = null;
		JsonArray output = null;
		
		if(accessToken == null || accessToken.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		LDAPUserInfo ldapUser = OpenstackClient.verifyAccessToken(accessToken);

		JsonObject key = OpenstackClient.adminAuth(this.getSwiftCredentials());	
		
//		LOGG
		System.out.println(ldapUser);
		this.logKeystoneAuthResponse(key);
		
		if(key != null) {
			output =  OpenstackClient.getUploadedFiles(key.get("X-Auth-Token").getAsString(), key.get("tenant-id").getAsString(), ldapUser.getPreferred_username());
		}
		
		r = Response.ok(output).status(Status.OK);
		return r.build();
	}
	
	/**
	 * @return
	 */
	protected static SMessageAuth getSwiftCredentials(){
		SMessageAuth smessage = new SMessageAuth();
		smessage.setPassword("swift");
		smessage.setService("service");
		smessage.setUsername("swift");
		return smessage;
	}
	
	/**
	 * @param key
	 */
	private void logKeystoneAuthResponse(JsonObject key){
		System.out.println("Token:  " + key.get("X-Auth-Token").getAsString());
		System.out.println("SID:    " + key.get("tenant-id").getAsString());
	}
	
}
