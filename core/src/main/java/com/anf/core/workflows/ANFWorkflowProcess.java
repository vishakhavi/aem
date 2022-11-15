package com.anf.core.workflows;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;

@Component(
        service = WorkflowProcess.class,
        immediate = true,
        property = {
                "process.label" + " = ANF Workflow Process",
                Constants.SERVICE_DESCRIPTION + " = Add property on page creation"
        }
)
public class ANFWorkflowProcess implements WorkflowProcess {

    private static final Logger log = LoggerFactory.getLogger(ANFWorkflowProcess.class);

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap processArguments) {
        log.info("\n Page property workflow process ");
        try {
            WorkflowData workflowData = workItem.getWorkflowData();
            Session session = workflowSession.adaptTo(Session.class);
            String path = workflowData.getPayload().toString() + "/jcr:content";
            Node node = (Node) session.getItem(path);
                if(node!=null){
                    node.setProperty("pageCreated",true);
                }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
