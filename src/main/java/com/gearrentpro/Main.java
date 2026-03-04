package com.gearrentpro;

import com.gearrentpro.ui.LoginScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
