package de.dbis.acis.cloud.Tethys.resource;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


@Path("/hello")
@Api(value="/hello", description = "A description of our HelloResource")
public class HelloResource {

	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value="Returns Hello World!")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response getAllServices() {
		return Response.ok("Hello World").status(Status.OK).build();
	}
	
}
