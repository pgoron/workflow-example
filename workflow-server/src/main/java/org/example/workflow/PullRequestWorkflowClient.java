package org.example.workflow;

import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
import org.example.workflow.api.PullRequest;
import org.example.workflow.api.PullRequestWorkflow;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PullRequestWorkflowClient {
    public static void main(String[] args) {
        WorkflowClient wc = WorkflowClient.newInstance(Constants.ORCHESTRATOR_IP, 7933, Constants.DOMAIN);

        PullRequestWorkflow wkf = wc.newWorkflowStub(PullRequestWorkflow.class, new WorkflowOptions.Builder()
                .setWorkflowId("John's PR - " + LocalDateTime.now().format(DateTimeFormatter.ISO_TIME))
                .setExecutionStartToCloseTimeout(Duration.ofSeconds(60))
                .setTaskList(Constants.DEFAULT_QUEUE)
                .build());

        PullRequest pr = new PullRequest();
        pr.setId("12");
        pr.setOwner("John Doe");
        pr.setSourceBranch("master");
        pr.setTargetBranch("master");
        pr.setSourceRepo("githum.com/toto");
        pr.setTargetRepo("githum.com/titi");
        System.out.println("Launching a new PR workflow");
        wkf.runPullRequest(pr);
    }
}
