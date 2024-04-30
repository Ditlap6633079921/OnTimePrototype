package Application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import Task.MustDoTask;
import Task.RecurringTask;
import Task.SideTask;
import Task.Task;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ToDoListApp extends Application {

    private ObservableList<Task> mustDoSideTasks;
    private ObservableList<Task> recurringTasks;
    private ListView<Task> mustDoSideTasksListView;
    private ListView<Task> recurringTasksListView;
    private Stage primaryStage;

    public ToDoListApp(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage.setTitle("To-do List");

        // Create list views for Must Do and Side Tasks
        mustDoSideTasksListView = new ListView<>();
        mustDoSideTasks = FXCollections.observableArrayList();
        mustDoSideTasksListView.setItems(mustDoSideTasks);
        mustDoSideTasksListView.setPrefWidth(300); // Increased width by 20%
        mustDoSideTasksListView.setPrefHeight(200); // Adjusted height
        mustDoSideTasksListView.setCellFactory(param -> new TaskListCell());

        // Create list view for Recurring Tasks
        recurringTasksListView = new ListView<>();
        recurringTasks = FXCollections.observableArrayList();
        recurringTasksListView.setItems(recurringTasks);
        recurringTasksListView.setPrefWidth(300); // Increased width by 20%
        recurringTasksListView.setPrefHeight(200); // Adjusted height
        recurringTasksListView.setCellFactory(param -> new TaskListCell());

        // Create the input fields and buttons
        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter a task");
        taskInput.setPrefWidth(160);
        DatePicker deadlinePicker = new DatePicker();
        deadlinePicker.setPrefWidth(80);
        DatePicker startDatePicker = new DatePicker(); // Start date picker for recurring tasks
        startDatePicker.setPrefWidth(80);
        startDatePicker.setDisable(true); // Initially disabled
        ComboBox<String> taskTypeComboBox = new ComboBox<>();
        taskTypeComboBox.getItems().addAll("Must Do", "Side Task", "Recurring Task");
        taskTypeComboBox.setValue("Must Do");
        taskTypeComboBox.setPrefWidth(100);
        Button addButton = new Button("Add");
        addButton.setPrefWidth(40);
        addButton.setOnAction(e -> addTask(taskInput.getText(), deadlinePicker.getValue(), startDatePicker.getValue(), taskTypeComboBox.getValue()));
        Button removeButton = new Button("Remove");
        removeButton.setPrefWidth(40);
        removeButton.setOnAction(e -> removeTask());

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(taskInput, deadlinePicker, startDatePicker, taskTypeComboBox, addButton, removeButton);
        inputBox.setPadding(new Insets(10));

        // Create VBox to hold Must Do and Side Tasks description and deadline
        VBox mustDoSideTasksBox = new VBox(10);
        mustDoSideTasksBox.getChildren().addAll(new Label("Must Do / Side Tasks"), mustDoSideTasksListView);
        mustDoSideTasksBox.setPadding(new Insets(10));

        // Create VBox to hold Recurring Tasks description and deadline
        VBox recurringTasksBox = new VBox(10);
        recurringTasksBox.getChildren().addAll(new Label("Recurring Tasks"), recurringTasksListView);
        recurringTasksBox.setPadding(new Insets(10));

        // Create the root layout
        BorderPane root = new BorderPane();
        root.setLeft(mustDoSideTasksBox);
        root.setRight(recurringTasksBox);
        root.setBottom(inputBox);

        // Create the dashboard scene
        Scene scene = new Scene(root, 600, 300); // Adjusted width by 20%

        // Set the fixed size for the scene
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void addTask(String taskDescription, LocalDate deadline, LocalDate startDate, String taskType) {
        if (!taskDescription.isEmpty() && deadline != null && (taskType.equals("Recurring Task") || startDate == null)) {
            Task newTask = null;
            switch (taskType) {
                case "Must Do":
                    newTask = new MustDoTask(taskDescription, deadline);
                    mustDoSideTasks.add(newTask);
                    break;
                case "Side Task":
                    newTask = new SideTask(taskDescription, deadline);
                    mustDoSideTasks.add(newTask);
                    break;
                case "Recurring Task":
                    newTask = new RecurringTask(taskDescription, deadline, startDate, "");
                    recurringTasks.add(newTask);
                    break;
            }
            if (newTask != null) {
                // Sort tasks by date after adding new task
                mustDoSideTasks.sort(Comparator.comparing(Task::getDeadline));
                recurringTasks.sort(Comparator.comparing(Task::getDeadline));
            }
        }
    }

    private void removeTask() {
        int selectedIndexMustDoSide = mustDoSideTasksListView.getSelectionModel().getSelectedIndex();
        int selectedIndexRecurring = recurringTasksListView.getSelectionModel().getSelectedIndex();

        if (selectedIndexMustDoSide != -1) {
            mustDoSideTasks.remove(selectedIndexMustDoSide);
        } else if (selectedIndexRecurring != -1) {
            recurringTasks.remove(selectedIndexRecurring);
        }
    }

    // Custom ListCell to display task descriptions and deadlines
    private class TaskListCell extends ListCell<Task> {
        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);
            if (empty || task == null) {
                setText(null);
            } else {
                setText(task.getDescription() + " - Deadline: " + task.getDeadline().toString());
            }
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
