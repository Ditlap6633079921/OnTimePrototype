package Task;

import java.time.LocalDate;

public class SideTask extends Task {
    public SideTask(String taskDescription, LocalDate deadline) {
        super(taskDescription, deadline, "Side Task");
    }

    @Override
    public void doTask() {
        // Implementation specific to SideTask
        System.out.println("Completing side task: " + getDescription());
    }
}
