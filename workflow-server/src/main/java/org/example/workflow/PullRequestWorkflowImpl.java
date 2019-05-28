package org.example.workflow;

import com.uber.cadence.activity.ActivityOptions;
import com.uber.cadence.common.RetryOptions;
import com.uber.cadence.worker.Worker;
import com.uber.cadence.workflow.Async;
import com.uber.cadence.workflow.Promise;
import com.uber.cadence.workflow.Workflow;
import org.example.workflow.api.BuildActivity;
import org.example.workflow.api.BuildResult;
import org.example.workflow.api.PullRequest;
import org.example.workflow.api.PullRequestWorkflow;

import java.time.Duration;

public class PullRequestWorkflowImpl implements PullRequestWorkflow {


    @Override
    public void runPullRequest(PullRequest pr) {
        BuildActivity buildActivity = Workflow.newActivityStub(BuildActivity.class, new ActivityOptions.Builder()
                .setTaskList("default")
                .setRetryOptions(new RetryOptions.Builder().setBackoffCoefficient(2.0).setMaximumAttempts(3).setInitialInterval(Duration.ofSeconds(1)).build())
                .setHeartbeatTimeout(Duration.ofMillis(2100))
                .setScheduleToCloseTimeout(Duration.ofSeconds(30))
                .build());

        System.out.println("Starting pull request " + pr.getId() + " from " + pr.getOwner());
        Promise<BuildResult> checkBuild = Async.function(buildActivity::build, pr);
        Promise<BuildResult> checkMerge = Async.function(buildActivity::build, pr);
        Promise<BuildResult> checkOther = Async.function(buildActivity::build, pr);

        Promise all = Promise.allOf(checkBuild, checkMerge, checkOther);
        all.get();
        BuildResult br = checkBuild.get();
        System.out.println("Build IsSuccessful: " + br.getIsSuccessful());
        System.out.println("Build ErrorCount: " + br.getErrorCount());
    }

    public static void main(String[] args) {
        Worker.Factory factory = new Worker.Factory(Constants.ORCHESTRATOR_IP, 7933, Constants.DOMAIN);
        Worker worker = factory.newWorker(Constants.DEFAULT_QUEUE);
        worker.registerWorkflowImplementationTypes(PullRequestWorkflowImpl.class);
        factory.start();
    }

}
