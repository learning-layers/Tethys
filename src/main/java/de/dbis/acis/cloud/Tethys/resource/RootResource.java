package de.dbis.acis.cloud.Tethys.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.*;


/**
 * This Resource matches the Root URI
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Path("/")
@Api("")
public class RootResource {

    /**
     * returns "Gives a description and a link where you can find more information about this API."
     * 
     * @return "Gives a description and a link where you can find more information about this API."
     */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getRoot() {
		return "<html>"+
				"<body>"+
				"This is the i5Cloud API home. For information on how to access the APIs, please visit <a href='http://developer.learning-layers.eu/documentation/tethys/'>the Learning Layers developers website.</a>"+
				"</body>"+
				"</html>";
	}
	
}