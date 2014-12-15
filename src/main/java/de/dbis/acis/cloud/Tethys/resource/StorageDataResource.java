//package de.dbis.acis.cloud.Tethys.resource;
//
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
////import javax.servlet.ServletOutputStream;
////import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.HeaderParam;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.StreamingOutput;
//
//import com.google.gson.JsonArray;
//import com.sun.jersey.api.client.ClientResponse.Status;
//import com.sun.jersey.multipart.FormDataParam;
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
//@Api("")
//public class StorageDataResource {
//
//	private ServicesJpa servicesService;
//	
//	/**
//	 * Class Constructor
//	 */
//	public StorageDataResource() {
//		servicesService = new ServicesJpaImpl();
//	}
//	
//
//	/**
//	 * Uploads a file to swift.
//	 * 
//	 * TODO
//	 * 
//	 * @param sid			the service-id in the URL
//	 * @param path			the path behind "/data/" in the URL
//	 * @param xAuthToken	the X-Auth-Token from the Header 
//	 * @param is			the data as InputStream
//	 * @return whether the data could be uploaded or not
//	 * @throws IOException
//	 */
//	@PUT
//	@Path("/{path : .+}")
//	@Consumes( { MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED } )
//	@ApiOperation(value="Returns the details of a service.")
//	@ApiResponses( {
//		@ApiResponse(code = 200, message = "OK")
//	} )
//	public Response testUploadFile(@PathParam("service-id") int sid, @PathParam("storage") String storage, @PathParam("path") String path, @HeaderParam("X-Auth-Token") String xAuthToken, InputStream is) throws IOException {
//		
//		Response.ResponseBuilder r = null;
//		Services service = null;
//		
//		service = servicesService.getServiceById(sid);
//		if(service != null) {
//			r =  OpenstackClient.uploadFile(is, xAuthToken, service.getTenantID(), storage+"/"+path);
//		}
//		
//		return r.build();
//		
//	}
//	
//	/**
//	 * Returns an array of all uploaded data.
//	 * 
//	 * TODO
//	 * 
//	 * @param sid			the service-id in the URL
//	 * @param xAuthToken	the X-Auth-Token from the Header 
//	 * @return	a Json-array of all uploaded data
//	 */
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiOperation(value="Returns the details of a service.")
//	@ApiResponses( {
//		@ApiResponse(code = 200, message = "OK")
//	} )
//	public Response getUploadedFiles(@PathParam("service-id") int sid, @PathParam("storage") String storage, @HeaderParam("X-Auth-Token") String xAuthToken) {
//		//curl  -X GET  -H 'X-Auth-Token: a992bfbf45194283aba5473bd40732c1' http://137.226.58.2:8888/v1/AUTH_d34a0c1691fd4bf6b89214e2731c0b33/data/
//		
//		Response.ResponseBuilder r = null;
//		Services service = null;
//		JsonArray output = null;
//		
//		service = servicesService.getServiceById(sid);
//		if(service != null) {
//			output =  OpenstackClient.getUploadedFiles(xAuthToken, service.getTenantID(), storage);
//		}
//		
//		r = Response.ok(output).status(Status.OK);
//		return r.build();
//
//	}
//	
//	
////	/**
////	 * Returns the data - Unfinished.
////	 * 
////	 * TODO
////	 * 
////	 * @param sid			the service-id in the URL
////	 * @param path			the path behind "/data/" in the URL
////	 * @param xAuthToken	the X-Auth-Token from the Header 
////	 * @return the data
////	 * @throws ClassNotFoundException
////	 * @throws IOException
////	 */
////	@GET
////	@Path("/{path : .+}")
////	//@Produces(MediaType.TEXT_HTML)
////	@ApiOperation(value="Returns the details of a service.")
////	@ApiResponses( {
////		@ApiResponse(code = 200, message = "OK")
////	} )
////	public Response getFile(@PathParam("service-id") int sid, @PathParam("storage") String storage, @PathParam("path") String path, @HeaderParam("X-Auth-Token") String xAuthToken, @Context HttpServletResponse response) throws ClassNotFoundException, IOException {
////		//curl  -X GET  -H 'X-Auth-Token: a992bfbf45194283aba5473bd40732c1' http://137.226.58.2:8888/v1/AUTH_d34a0c1691fd4bf6b89214e2731c0b33/data/1/es.geht.html > test.file
////		Services service = null;
////		service = servicesService.getServiceById(sid);
////		if(service == null) {
////			return Response.status(Status.NOT_FOUND).build();
////		}
////
////		try {
////			ServletOutputStream bos = response.getOutputStream();
////			OpenstackClient.getFile(bos,xAuthToken, service.getTenantID(), storage+"/"+path);
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////			
////		return Response.ok().build();	
////
////	}
//	
//}
//
