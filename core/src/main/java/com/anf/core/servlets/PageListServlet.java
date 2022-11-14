package com.anf.core.servlets;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.*;

@Component(service = {Servlet.class})
@SlingServletPaths(value = "/bin/pageList")
@ServiceDescription("Top 10 page list servlet")
public class PageListServlet extends SlingSafeMethodsServlet {

    /**
     * Generated serialVersionUID
     */
    private static final long serialVersionUID = 2610051404257637265L;

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(PageListServlet.class);

    /**
     * Injecting the QueryBuilder dependency
     */
    @Reference
    private QueryBuilder builder;

    /**
     * Session object
     */
    private Session session;

    /**
     * Overridden doGet() method
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        log.info("----------< Executing PageList Servlet >----------");
        /**
         * Get resource resolver instance
         */
        ResourceResolver resourceResolver = request.getResourceResolver();

        /**
         * Adapting the resource resolver to the session object
         */
        session = resourceResolver.adaptTo(Session.class);

        log.info("selectors {}", request.getRequestPathInfo().getSelectorString());
        log.info("pathinfo {}", request.getPathInfo());
        try {
            if (request.getRequestPathInfo().getSelectorString().equalsIgnoreCase("querybuilder")) {
                getQueryBuilderResults(response);

            } else if (request.getRequestPathInfo().getSelectorString().equalsIgnoreCase("xpath")) {
                final String xpathQuery =
                        "/jcr:root/content/anf-code-challenge/us/en//element(*, cq:Page)\n" +
                                "[\n" +
                                "(jcr:content/@anfCodeChallenge = 'true')\n" +
                                "]\n" +
                                "order by jcr:content/@jcr:created";
                Iterator<Resource> result = resourceResolver.findResources(xpathQuery, javax.jcr.query.Query.XPATH);
                List<String> pagePaths = new ArrayList<>();
                result.forEachRemaining(resource -> pagePaths.add(resource.getPath()));
                response.setContentType("text/plain");
                Gson gson = new Gson();
                response.getWriter().println(gson.toJson(pagePaths));
            } else {
                response.getWriter().println("No results");
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    private void getQueryBuilderResults(SlingHttpServletResponse response) throws RepositoryException, IOException {
        /**
         * Map for the predicates
         */
        Map<String, String> predicate = new HashMap<>();

        /**
         * Configuring the Map for the predicate
         */
        predicate.put("path", "/content/anf-code-challenge/us/en");
        predicate.put("type", "cq:Page");
        predicate.put("1_property", JcrConstants.JCR_CONTENT + "/anfCodeChallenge");
        predicate.put("1_property.value", "true");
        predicate.put("orderby", JcrConstants.JCR_CONTENT + "/" + JcrConstants.JCR_CREATED);
        predicate.put("p.limit", "10");

        /**
         * Creating the Query instance
         */
        Query query = builder.createQuery(PredicateGroup.create(predicate), session);

        /**
         * Getting the search results
         */
        SearchResult searchResult = query.getResult();

        for (Hit hit : searchResult.getHits()) {
            String path = hit.getPath();
            response.getWriter().println(path);
        }
    }

}