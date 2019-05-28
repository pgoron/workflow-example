package org.example.workflow.api;

import com.uber.cadence.workflow.WorkflowMethod;

public interface PullRequestWorkflow {
    @WorkflowMethod
    void runPullRequest(PullRequest pr);
}
