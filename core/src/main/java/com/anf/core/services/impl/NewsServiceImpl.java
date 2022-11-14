package com.anf.core.services.impl;

import com.anf.core.services.NewsService;
import com.anf.core.util.ResolverUtil;
import com.anf.core.util.ServiceUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component(immediate = true, service = NewsService.class)
public class NewsServiceImpl implements NewsService {
    private static final Logger LOG = LoggerFactory.getLogger(NewsServiceImpl.class);
    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public void getNews(String path) {

        String nodeLocation = "/var/commerce/products/anf-code-challenge/newsData";
        try {
            ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
            Session session=resourceResolver.adaptTo(Session.class);
            Iterator<Resource> news=resourceResolver.getResource(nodeLocation).listChildren();
            Node newsRoot = session.getNode(path).getNode("news");
           // Node newsNode = newsRoot.addNode("news","nt:unstructured");
            if(newsRoot != null) {
                while (news.hasNext()){
                    Resource resource=news.next();
                    Node itemNode = newsRoot.addNode(resource.getName(),"nt:unstructured");
                    // Map<String,String> news=new HashMap<>();
                    ValueMap prop=resource.getValueMap();
                    itemNode.setProperty("author", ServiceUtil.getProprty(prop,"author"));
                    itemNode.setProperty("content", ServiceUtil.getProprty(prop,"content"));
                    itemNode.setProperty("title",ServiceUtil.getProprty(prop,"title"));
                    itemNode.setProperty("description",ServiceUtil.getProprty(prop,"description"));
                    itemNode.setProperty("url",ServiceUtil.getProprty(prop,"url"));
                    itemNode.setProperty("urlImage",ServiceUtil.getProprty(prop,"urlImage"));
                   // newsList.add(news);
                    session.save();
                }
            }

        } catch (Exception e) {
            LOG.error("Occurred exception - {}", e.getMessage());
        }
    }
}
