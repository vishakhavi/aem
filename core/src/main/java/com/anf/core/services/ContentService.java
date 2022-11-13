package com.anf.core.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface ContentService {
	boolean commitUserDetails(String firstname, String lastname, String age, String country, ResourceResolver resourceResolver);
}
