package de.dbis.acis.cloud.Tethys;

import javax.ws.rs.client.ClientBuilder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import de.dbis.acis.cloud.Tethys.proxy.openstack.keystone.v2_0.ProxyKeystoneVersions;
import de.dbis.acis.cloud.Tethys.util.ProxyFactory;

public class TethysBinder extends AbstractBinder {

	@Override
	protected void configure() {
		//bindFactory(new ProxyFactory<ProxyKeystoneVersions>(ProxyKeystoneVersions.class)).to(ProxyKeystoneVersions.class);
		bind(WebResourceFactory.newResource(ProxyKeystoneVersions.class, ClientBuilder.newClient().target("http://137.226.58.2:5000"))).to(ProxyKeystoneVersions.class);
	}

}
