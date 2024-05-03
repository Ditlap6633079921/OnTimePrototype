package Application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Page.MainPage;

public class ToDoListApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("To-do List");

        VBox root = new VBox();
        MainPage mainPage = new MainPage(root);

        // Create the dashboard scene
        Scene scene = new Scene(root, 600, 300);

        // Set the fixed size for the scene
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
