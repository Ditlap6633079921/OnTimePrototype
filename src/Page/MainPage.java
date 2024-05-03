package Page;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import Task.MustDoTask;
import Task.RecurringTask;
import Task.SideTask;
import Task.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class MainPage {

    private ObservableList<Task> allTasks;
    private ListView<Task> allTasksListView;
    private DatePicker startDatePicker;

    public MainPage(VBox root) {
        allTasks = FXCollections.observableArrayList();
        allTasksListView = new ListView<>();
        allTasksListView.setItems(allTasks);
        allTasksListView.setPrefWidth(600); // Set width to accommodate all tasks
        allTasksListView.setPrefHeight(200); // Adjusted height
        allTasksListView.setCellFactory(param -> new TaskListCell());

        // Create the input fields and buttons
        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter a task");
        taskInput.setPrefWidth(160);
        startDatePicker = new DatePicker(); // Start date picker for recurring tasks
        startDatePicker.setPrefWidth(80);
        startDatePicker.setDisable(true); // Initially disabled
        DatePicker endDatePicker = new DatePicker(); // End date picker for all tasks
        endDatePicker.setPrefWidth(80);
        ComboBox<String> taskTypeComboBox = new ComboBox<>();
        taskTypeComboBox.getItems().addAll("Must Do", "Side Task", "Recurring Task");
        taskTypeComboBox.setValue("Must Do");
        taskTypeComboBox.setPrefWidth(100);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addTask(taskInput.getText(), startDatePicker.getValue(), endDatePicker.getValue(), taskTypeComboBox.getValue()));
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> removeTask());

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(taskInput, new Label("Start Date:"), startDatePicker, new Label("End Date:"), endDatePicker, new Label("Task Type:"), taskTypeComboBox, addButton, removeButton);
        inputBox.setPadding(new Insets(10));

        // Add event listener to taskTypeComboBox to enable/disable startDatePicker
        taskTypeComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.equals("Recurring Task")) {
                startDatePicker.setDisable(false);
            } else {
                startDatePicker.setDisable(true);
            }
        });

        // Add components to the root layout
        root.getChildren().addAll(new Label("All Tasks"), allTasksListView, inputBox);
        root.setPadding(new Insets(10));
    }

    private void addTask(String taskDescription, LocalDate startDate, LocalDate endDate, String taskType) {
        if (!taskDescription.isEmpty() && endDate != null) {
            Task newTask = null;
            switch (taskType) {
                case "Must Do":
                    newTask = new MustDoTask(taskDescription, endDate); // For Must Do, use end date as deadline
                    break;
                case "Side Task":
                    newTask = new SideTask(taskDescription, endDate); // For Side Task, use end date as deadline
                    break;
                case "Recurring Task":
                    if (startDate != null && endDate != null) {
                        newTask = new RecurringTask(taskDescription, endDate, startDate, endDate, ""); // For Recurring, use both start and end date
                    }
                    break;
            }
            if (newTask != null) {
                allTasks.add(newTask);
                // Sort tasks by date after adding new task
                allTasks.sort(Comparator.comparing(Task::getDeadline));
            }
        }
    }

    private void removeTask() {
        int selectedIndex = allTasksListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            allTasks.remove(selectedIndex);
        }
    }

    // Custom ListCell to display task descriptions, deadlines, and types, with overdue tasks in red
    private class TaskListCell extends ListCell<Task> {
        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);
            if (empty || task == null) {
                setText(null);
            } else {
                String description = task.getDescription();
                String type = task instanceof MustDoTask ? "Must Do" : task instanceof SideTask ? "Side Task" : "Recurring Task";
                String deadlineString = task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String text = description + " (" + type + ") - Deadline: " + deadlineString;
                setText(text);

                // If the task is overdue, set text to red
                if (task.getDeadline().isBefore(LocalDate.now())) {
                    setStyle("-fx-text-fill: red;");
                } else {
                    setStyle(""); // Reset style
                }
            }
        }
    }
}
