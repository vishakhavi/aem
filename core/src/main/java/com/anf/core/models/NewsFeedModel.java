package com.anf.core.models;

import com.anf.core.beans.News;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.service.component.annotations.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewsFeedModel {

    @ValueMapValue
    @Default(values = "/var/commerce/products/anf-code-challenge")
    public String newsFeedPath;

    private List<News> newsList = new ArrayList();

    @SlingObject
    private ResourceResolver resourceResolver;

    @PostConstruct
    public void init(){
        Resource resourceData = resourceResolver.getResource(newsFeedPath+"/newsData");

        if(resourceData != null){
            Iterator<Resource> newsFeedItems = resourceData.listChildren();
            while(newsFeedItems.hasNext()){
                final Resource newsFeedItemResource = newsFeedItems.next();
                News newsFeed = getNewsAsBean(newsFeedItemResource);
                newsList.add(newsFeed);
            }
        }
    }

    private News getNewsAsBean(Resource newsFeedItemResource) {

        ValueMap valueMap = newsFeedItemResource.getValueMap();
        News newsItem = new News();

        newsItem.setAuthor(valueMap.get("author", String.class));
        newsItem.setContent(valueMap.get("content", String.class));
        newsItem.setDescription(valueMap.get("description", String.class));
        newsItem.setTitle(valueMap.get("title", String.class));
        newsItem.setUrl(valueMap.get("url", String.class));
        newsItem.setUrlImage(valueMap.get("urlImage", String.class));
        newsItem.setDate(getCurrentDate());

        return newsItem;
    }

    private String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(new Date());
    }

    public String getNewsFeedPath() {
        return newsFeedPath;
    }

    public List<News> getNewsList() {
        return this.newsList;
    }

}
