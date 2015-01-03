package de.dbis.acis.cloud.Tethys.resource;

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

import com.google.gson.JsonObject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.dbis.acis.cloud.Tethys.client.OpenstackClient;
import de.dbis.acis.cloud.Tethys.entity.Services;
import de.dbis.acis.cloud.Tethys.jpa.ServicesJpa;
import de.dbis.acis.cloud.Tethys.jpa.impl.ServicesJpaImpl;

/**
 * This SubResource matches the URL i5Cloud/services/{service-id}/instances
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Path("/services/{service-id}/instances")
@Api(value="/services/{service-id}/instances", description = "Operations about instances")
public class InstancesResource {
	
	private ServicesJpa servicesService;
	
	// TODO hardcodet!?
	private JsonObject imageRef = new JsonObject();
	private JsonObject flavorRef = new JsonObject();

	/**
	 * Class Constructor
	 */
	public InstancesResource() {
		servicesService = new ServicesJpaImpl();
		// TODO hardcodet!?
		imageRef.addProperty("imageRef", "398fa70f-aa93-4e86-b9d8-a9a5a8b5f486");
		flavorRef.addProperty("flavorRef", "2bbf65df-bb63-4454-a405-10671aefda6d");
	}
	
	@Path("/{instance-id}")
    public InstanceResource getServiceData( @Context ResourceContext rc) {
		return rc.getResource(InstanceResource.class);
    }
	
	@GET
	@Produces( { MediaType.APPLICATION_JSON } )
	@ApiOperation(value="Returns all Images & Snapshots.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response getInstances(@PathParam("service-id") int sid,@HeaderParam("X-Auth-Token") String xAuthToken) {
		
		JsonObject output = null;
		Services service = servicesService.getServiceById(sid);
		
		if( service==null ) { 
			return Response.ok().status(Status.NOT_FOUND).build();
		}		
		
		output = OpenstackClient.getInstances(xAuthToken, service.getTenantID());
		
		return Response.ok(output).status(Status.OK).build();
	}
	
	
	/**
	 * Creates Instances.
	 * <p>
	 * example: curl -v -X POST http://137.226.58.11:8081/i5Cloud/services/{service-id}/instances -H 'X-Auth-Token: <X-Auth-Token>' -H 'Content-Type: application/json' -d '{"name":"", "flavor":""}'
	 * <p>
	 * input		{"name":"", "flavor":""}
	 * <p>
	 * Responses:
	 * <ul>
	 *		<li>201 Created - everything worked</li>
	 *		<li>406 Not Acceptable - the json formation is wrong</li>
	 * </ul>
	 * 
	 * @param sid			the service-id in the URL
	 * @param xAuthToken	the X-Auth-Token from the Header
	 * @param input			the Json-Object in the Request-Body
	 * @return whether the instance could be created or if the request fails and the complete Json send by Openstack.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Returns all Images & Snapshots.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response createInstance(@PathParam("service-id") int sid,@HeaderParam("X-Auth-Token") String xAuthToken, JsonObject input) {
		
		JsonObject output = null;
		Response.ResponseBuilder r = null;
		Services service = servicesService.getServiceById(sid);
		
		if( service==null || input == null || xAuthToken==null || !(input.has("name")) ) { 
			output = new JsonObject();
			output.addProperty("error", "wrong json structure");
			r = Response.ok(output).status(Status.NOT_ACCEPTABLE);
			return r.build();
		}		
		
		output = OpenstackClient.createInstance(xAuthToken, service.getTenantID(), input.get("name"),
				input.has("script")?input.get("script"):null,imageRef.get("imageRef"), input.has("flavor")?input.get("flavor"):flavorRef.get("flavorRef"));

						//flavorRef.get("flavorRef");
		if(output!=null){r = Response.ok(output).status(Status.CREATED);}
		else{r = Response.ok(output).status(Status.BAD_REQUEST);}
		
		return r.build();
		
	}
}
