package de.dbis.acis.cloud.Tethys.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.dbis.acis.cloud.Tethys.proxy.openstack.keystone.v2_0.ProxyKeystoneVersions;

/**
 * Class for Testing purposes.
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Path("/hello")
@Api(value="/hello", description = "A description of our HelloResource")
public class HelloResource {
	
	@Inject 
	ProxyKeystoneVersions proxyKeystoneVersion;
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value="Returns Hello World!")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response getAllServices() {
		return Response.ok("Hello World").status(Status.OK).build();
	}
	
	@GET
	@Path("/2")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAPIVersions() {
		return Response.ok(proxyKeystoneVersion.getAPIVersions()).build();
	}
	
}
