package Task;

import java.time.LocalDate;

public class MustDoTask extends Task {
    public MustDoTask(String taskDescription, LocalDate deadline) {
        super(taskDescription, deadline, "Must Do");
    }

    @Override
    public void doTask() {
        // Implementation specific to MustDoTask
        System.out.println("Completing must-do task: " + getDescription());
    }
}
