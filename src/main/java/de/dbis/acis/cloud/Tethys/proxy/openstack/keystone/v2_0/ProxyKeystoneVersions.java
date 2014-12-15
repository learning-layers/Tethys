package de.dbis.acis.cloud.Tethys.proxy.openstack.keystone.v2_0;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;

@Path("/")
public interface ProxyKeystoneVersions {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject getAPIVersions();
	
}
