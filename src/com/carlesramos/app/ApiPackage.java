package com.carlesramos.app;

import org.glassfish.jersey.server.ResourceConfig;

public class ApiPackage extends ResourceConfig{
	
	public ApiPackage() {
		packages("com.carlesramos.services");
	}

}
