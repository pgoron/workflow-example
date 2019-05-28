package org.example.workflow;

import com.uber.cadence.activity.Activity;
import com.uber.cadence.worker.Worker;
import com.uber.cadence.worker.WorkerOptions;
import org.example.workflow.api.BuildActivity;
import org.example.workflow.api.BuildResult;
import org.example.workflow.api.PullRequest;

public class BuildActivityImpl implements BuildActivity {
    @Override
    public BuildResult build(PullRequest pr) {
        System.out.println("Building " + pr.getId() + " from " + pr.getOwner());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Activity.heartbeat("building");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("OK");
        BuildResult br = new BuildResult();
        br.setIsSuccessful(true);
        br.setErrorCount(0);
        return br;
    }

    public static void main(String[] args) {
        Worker.Factory factory = new Worker.Factory(Constants.ORCHESTRATOR_IP, 7933, Constants.DOMAIN);
        Worker worker = factory.newWorker(Constants.DEFAULT_QUEUE, new WorkerOptions.Builder()
                .setMaxConcurrentActivityExecutionSize(1)
                .build());
        worker.registerActivitiesImplementations(new BuildActivityImpl());
        factory.start();
    }
}
