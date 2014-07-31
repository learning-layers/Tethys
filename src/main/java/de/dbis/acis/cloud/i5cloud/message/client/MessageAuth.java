package de.dbis.acis.cloud.i5cloud.message.client;

import de.dbis.acis.cloud.i5cloud.message.server.*;

public class MessageAuth {

	Auth auth;
	
	public MessageAuth(SMessageAuth message) {
		auth = new Auth(message);
	}
}
