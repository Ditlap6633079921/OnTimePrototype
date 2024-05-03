import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Application.ToDoListApp;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane root;
    private ToDoListApp toDoListApp;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("To-do List App");

        // Create the welcome scene
        root = new BorderPane();
        Scene welcomeScene = new Scene(root, 300, 200);

        Button getStartedButton = new Button("Get Started");
        getStartedButton.setOnAction(e -> goToDashboard());

        VBox welcomeBox = new VBox(10);
        welcomeBox.getChildren().add(getStartedButton);
        welcomeBox.setPadding(new Insets(10));
        root.setCenter(welcomeBox);

        // Show the welcome scene
        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }

    private void goToDashboard() {
        // Launch the ToDoListApp
        toDoListApp = new ToDoListApp();
        toDoListApp.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
