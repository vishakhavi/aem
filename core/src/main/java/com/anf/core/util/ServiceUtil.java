package com.anf.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;


public class ServiceUtil {


    public static String getRequestParamter(SlingHttpServletRequest request, String name){
        String requestParameter=request.getParameter(name);
        return requestParameter;
    }

    public static String getProprty(ValueMap valueMap, String prop){
        if(StringUtils.isNotBlank(valueMap.get(prop,String.class))){
            return valueMap.get(prop,String.class);
        }
        return "NA";
    }
}
