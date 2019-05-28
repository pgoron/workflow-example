package org.example.workflow;

import com.uber.cadence.worker.Worker;

public class WorkflowAndActivitiesInSameProcess {

    public static void main(String[] args) {
        // start worker that hosts workflow and activities
        Worker.Factory factory = new Worker.Factory(Constants.ORCHESTRATOR_IP, 7933, Constants.DOMAIN);
        Worker worker = factory.newWorker(Constants.DEFAULT_QUEUE);
        worker.registerActivitiesImplementations(new BuildActivityImpl());
        worker.registerWorkflowImplementationTypes(PullRequestWorkflowImpl.class);
        factory.start();

        PullRequestWorkflowClient.main(args);
    }
}
