package org.example.workflow.api;

public interface BuildActivity {
    BuildResult build(PullRequest pr);
}
