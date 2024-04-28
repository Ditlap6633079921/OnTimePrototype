package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import Task.MustDoTask;
import Task.RecurringTask;
import Task.SideTask;
import Task.Task;

import java.time.LocalDate;
import java.util.Comparator;

public class ToDoListApp {

    private ObservableList<Task> tasks;
    private ListView<Task> listView;

    public Scene createDashboardScene() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("To-do List");

        // Create the list view
        listView = new ListView<>();
        tasks = FXCollections.observableArrayList();
        listView.setItems(tasks);
        listView.setPrefSize(400, 200);

        // Set cell factory for custom rendering
        listView.setCellFactory(param -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                } else {
                    setText(task.toString());
                    // Set color based on task type
                    switch (task.getType()) {
                        case "Must Do":
                            setTextFill(javafx.scene.paint.Color.DARKRED);
                            break;
                        case "Recurring Task":
                            setTextFill(javafx.scene.paint.Color.DARKVIOLET);
                            break;
                        case "Side Task":
                            setTextFill(javafx.scene.paint.Color.NAVY);
                            break;
                        default:
                            setTextFill(javafx.scene.paint.Color.BLACK);
                            break;
                    }
                }
            }
        });

        // Create the input fields and buttons
        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter a task");
        taskInput.setPrefWidth(250);
        DatePicker deadlinePicker = new DatePicker();
        deadlinePicker.setPrefWidth(120);
        DatePicker startDatePicker = new DatePicker(); // Start date picker for recurring tasks
        startDatePicker.setPrefWidth(120);
        startDatePicker.setDisable(true); // Initially disabled
        ComboBox<String> taskTypeComboBox = new ComboBox<>();
        taskTypeComboBox.getItems().addAll("Must Do", "Side Task", "Recurring Task");
        taskTypeComboBox.setValue("Must Do");
        taskTypeComboBox.setPrefWidth(120);
        Button addButton = new Button("Add");
        addButton.setPrefWidth(60);
        addButton.setOnAction(e -> addTask(taskInput.getText(), deadlinePicker.getValue(), startDatePicker.getValue(), taskTypeComboBox.getValue()));
        Button removeButton = new Button("Remove");
        removeButton.setPrefWidth(60);
        removeButton.setOnAction(e -> removeTask());

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(taskInput, deadlinePicker, startDatePicker, taskTypeComboBox, addButton, removeButton);
        inputBox.setPadding(new Insets(10));

        // Create the root layout
        BorderPane root = new BorderPane();
        root.setCenter(listView);
        root.setBottom(inputBox);

        // Create the dashboard scene
        Scene scene = new Scene(root, 500, 300);

        // Set the fixed size for the scene
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        // Listener to enable/disable start date picker based on task type selection
        taskTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals("Recurring Task")) {
                startDatePicker.setDisable(false);
            } else {
                startDatePicker.setDisable(true);
            }
        });

        // Return the dashboard scene
        return scene;
    }

    private void addTask(String taskDescription, LocalDate deadline, LocalDate startDate, String taskType) {
        if (!taskDescription.isEmpty() && deadline != null && (taskType.equals("Recurring Task") || startDate == null)) {
            Task newTask = null;
            switch (taskType) {
                case "Must Do":
                    newTask = new MustDoTask(taskDescription, deadline);
                    break;
                case "Side Task":
                    newTask = new SideTask(taskDescription, deadline);
                    break;
                case "Recurring Task":
                    newTask = new RecurringTask(taskDescription, deadline, startDate, "");
                    break;
            }
            if (newTask != null) {
                tasks.add(newTask);

                // Sort tasks by date after adding new task
                tasks.sort(Comparator.comparing(Task::getDeadline));
            }
        }
    }

    private void removeTask() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            tasks.remove(selectedIndex);
        }
    }
}
