//package de.dbis.acis.cloud.Tethys.resource;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//
//
//import com.sun.jersey.api.core.ResourceContext;
//import com.wordnik.swagger.annotations.*;
//
//
///**
// * This Resource matches the URL /5Cloud
// * 
// * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
// */
//@Path("/")
//@Api("")
//public class RootResource {
////
////	/**
////	 * A link to the SubResource AuthResource.class
////	 * 
////	 * @see AuthResource
////	 * @param rc the context
////	 * @return the AuthResource.class
////	 */
////	@Path("/auth")
////    public Object getAuth(@Context ResourceContext rc) {
////        	return rc.getResource(AuthResource.class);
////    }
////	
////	/**
////	 * A link to the SubResource ServicesResource
////	 * 
////	 * @see ServicesResource
////	 * @param rc the context
////	 * @return the ServicesResource.class
////	 */
////	@Path("/services")
////    @ApiOperation(
////    		value = "Gets the owner of a pet", 
////    	    response = ServicesResource.class)
////    @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid ID supplied"),
////    	      @ApiResponse(code = 404, message = "Pet not found") })
////    public ServicesResource getServices() {// ResourceContext rc) {
////        	return new ServicesResource();//rc.getResource(ServicesResource.class);
////    }
////    
////	/**
////	 * A link to the SubResource UsersResource
////	 * 
////	 * @see UsersResource
////	 * @param rc the context
////	 * @return the UsersResource.class
////	 */
////    @Path("users")
////    public Object getUsers(@Context ResourceContext rc) {
////        	return rc.getResource(UsersResource.class);
////    }
////    
////    
//    /**
//     * returns "This is the root-URL of i5Cloud"
//     * 
//     * @return "This is the root-URL of i5Cloud"
//     */
//	@GET
//	@Produces(MediaType.TEXT_HTML)
//	public String getRoot() {
//		return "<html>"+
//				"<body>"+
//				"This is the i5Cloud API home. For information on how to access the APIs, please visit <a href='http://developer.learning-layers.eu/documentation/i5cloud/'>the Learning Layers developers website.</a>"+
//				"</body>"+
//				"</html>";
//	}
//	
//}