package Task;

import java.time.LocalDate;

public class RecurringTask extends Task {
    private String recurrencePattern;
    private LocalDate startDate;

    public RecurringTask(String taskDescription, LocalDate deadline, LocalDate startDate, LocalDate endDate, String recurrencePattern) {
        super(taskDescription, deadline, "Recurring Task");
        this.startDate = startDate;
        this.recurrencePattern = recurrencePattern;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public void doTask() {
        // Implementation specific to RecurringTask
        System.out.println("Completing recurring task: " + getDescription());
    }
}
