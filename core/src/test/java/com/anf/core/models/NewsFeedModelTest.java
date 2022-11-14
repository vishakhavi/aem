package com.anf.core.models;

import com.anf.core.beans.News;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class NewsFeedModelTest {
    private final AemContext aemContext = new AemContext();
    private static final String CURR_RES = "/var/newsList";
    News news;
    @InjectMocks
    NewsFeedModel model;

    @BeforeEach
    void setUp() throws Exception {
        aemContext.addModelsForClasses(NewsFeedModel.class);
        aemContext.load().json("/com/anf/core/models/news-feed.json", "/var");
        model = aemContext.currentResource("/var/newsList").adaptTo(NewsFeedModel.class);
    }

    @Test
    void init() {
        Resource resource= aemContext.currentResource("/var/data/newsData");
        assertTrue(resource.hasChildren());
        assertEquals(5,model.getNewsList().size());
    }

    @Test
    void getNewsFeedPath() {
        assertEquals("/var/data", model.getNewsFeedPath());
    }

}