package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MainApp class.
 *
 * The class that opens the GUI and connects the Client to the GUI
 *
 * @author Group 3
 * @version 0.1
 */

public class MainApp extends Application {
    Client client = new Client();
    public void init() throws Exception {
        super.init();

    }

    // starting the GUI
    @Override
    public void start(Stage primaryStage) throws Exception {

        // instead this code is used to load the View and Controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scene.fxml"));
        Parent root = loader.load();

        // obtaining the controller from the loader
        var controller = (FXMLController)loader.getController();
        controller.setModel(client);



        primaryStage.setTitle("Weather station by Group 3");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
