//package de.dbis.acis.cloud.Tethys.resource;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.HeaderParam;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import com.google.gson.JsonArray;
//import com.sun.jersey.api.client.ClientResponse.Status;
//import com.sun.jersey.api.core.ResourceContext;
//import com.wordnik.swagger.annotations.Api;
//import com.wordnik.swagger.annotations.ApiOperation;
//import com.wordnik.swagger.annotations.ApiParam;
//import com.wordnik.swagger.annotations.ApiResponse;
//import com.wordnik.swagger.annotations.ApiResponses;
//
//import de.dbis.acis.cloud.Tethys.client.OpenstackClient;
//import de.dbis.acis.cloud.Tethys.entity.Services;
//import de.dbis.acis.cloud.Tethys.jpa.ServicesJpa;
//import de.dbis.acis.cloud.Tethys.jpa.impl.ServicesJpaImpl;
//
///**
// * This SubResource matches the URL /i5Cloud/services/{service-id}/{container}
// * 
// * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de> *
// */
//@Path("/services/{service-id}/storage")
//@Api(value="/services/{service-id}/storage", description = "Operations about Files & Container")
//public class ServiceStorageResource {
//
//	private ServicesJpa servicesService;
//	
//	/**
//	 * Class Constructor
//	 */
//	public ServiceStorageResource() {
//		servicesService = new ServicesJpaImpl();
//	}
//	
//	
//	@Path("/{storage}")
//    public StorageDataResource getServiceData(@ApiParam(access = "internal")  @PathParam("storage")  String container, @Context ResourceContext rc) {
//    	switch(container){
//    		case "private":
//    		case "public":
//    		case "data":
//    			return rc.getResource(StorageDataResource.class);
//    		default:
//    			return null;	
//    	}
//    }
//	
//	/**
//	 * Returns an array of all uploaded data.
//	 * 
//	 * TODO NEED TO IMPROVE
//	 * 
//	 * @param sid			the service-id in the URL
//	 * @param xAuthToken	the X-Auth-Token from the Header 
//	 * @return	a Json-array of all uploaded data
//	 */
//	@GET
//	@ApiOperation(value="Returns the details of a service.")
//	@ApiResponses( {
//		@ApiResponse(code = 200, message = "OK")
//	} )
//	public String getUploadedFiles(@PathParam("service-id") int sid, @HeaderParam("X-Auth-Token") String xAuthToken) {
//		//curl  -X GET  -H 'X-Auth-Token: a992bfbf45194283aba5473bd40732c1' http://137.226.58.2:8888/v1/AUTH_d34a0c1691fd4bf6b89214e2731c0b33/data/
//		
////		Response.ResponseBuilder r = null;
////		Services service = null;
////		JsonArray output = null;
////		
////		service = servicesService.getServiceById(sid);
////		if(service != null) {
////			output =  OpenstackClient.getUploadedFiles(xAuthToken, service.getTenantID(),"");
////		}
////		
////		r = Response.ok(output).status(Status.OK);
////		return r.build();
//		return "private, public, data";
//
//	}
//}
