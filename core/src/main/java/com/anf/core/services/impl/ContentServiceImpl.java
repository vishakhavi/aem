package com.anf.core.services.impl;

import com.anf.core.services.ContentService;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = ContentService.class)
public class ContentServiceImpl implements ContentService {
    private static final Logger LOG = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Override
    public boolean commitUserDetails(String firstname, String lastname, String age, String country, ResourceResolver resourceResolver) {
        // Add your logic. Modify method signature as per need.
        String nodeLocation = "/var/anf-code-challenge";
        try {
            Resource source = ResourceUtil.getOrCreateResource(resourceResolver, nodeLocation,
                    Collections.singletonMap("jcr:primaryType", (Object) "sling:OrderedFolder"),
                    null, false);
            Map<String, Object> userInputValues = getUserInputValuesMap(firstname, lastname, age, country);
            resourceResolver.create(source, "data_" + new Date().getTime(), userInputValues);
            resourceResolver.commit();
            return true;
        } catch (PersistenceException err) {
            LOG.error(err.getMessage(), err);
        } finally {
            if (resourceResolver != null)
                resourceResolver.close();
        }
        return false;
    }

    private static Map<String, Object> getUserInputValuesMap(String firstName, String lastName, String age, String country) {
        Map<String, Object> userInputValues = new HashMap<>();
        userInputValues.put("jcr:primaryType", (Object) "nt:unstructured");
        userInputValues.put("firstName", firstName);
        userInputValues.put("lastName", lastName);
        userInputValues.put("age", age);
        userInputValues.put("country", country);
        return userInputValues;
    }
}
