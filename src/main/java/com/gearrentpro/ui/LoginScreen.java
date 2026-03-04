package com.gearrentpro.ui;

import com.gearrentpro.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginScreen {
    private final AuthService authService;

    public LoginScreen() {
        this.authService = new AuthService();
    }

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - Login");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f5f5f5;");

        Label titleLabel = new Label("GearRent Pro");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label subtitleLabel = new Label("Equipment Rental Management System");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d;");

        GridPane formPane = new GridPane();
        formPane.setAlignment(Pos.CENTER);
        formPane.setHgap(10);
        formPane.setVgap(15);
        formPane.setPadding(new Insets(30));
        formPane.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        formPane.setMaxWidth(400);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setPrefWidth(250);
        usernameField.setStyle("-fx-padding: 8; -fx-font-size: 13;");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setPrefWidth(250);
        passwordField.setStyle("-fx-padding: 8; -fx-font-size: 13;");

        Label messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(250);
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(250);
        loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 10;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 10;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 10;"));

        formPane.add(usernameLabel, 0, 0);
        formPane.add(usernameField, 0, 1);
        formPane.add(passwordLabel, 0, 2);
        formPane.add(passwordField, 0, 3);
        formPane.add(messageLabel, 0, 4);
        formPane.add(loginButton, 0, 5);

        Label infoLabel = new Label("Default credentials:\nAdmin: admin / password123\nManager: manager_pan / password123\nStaff: staff_pan / password123");
        infoLabel.setFont(Font.font("Arial", 11));
        infoLabel.setStyle("-fx-text-fill: #95a5a6; -fx-text-alignment: center;");
        infoLabel.setAlignment(Pos.CENTER);

        root.getChildren().addAll(titleLabel, subtitleLabel, formPane, infoLabel);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter username and password");
                return;
            }

            try {
                if (authService.login(username, password)) {
                    messageLabel.setText("");
                    DashboardScreen dashboard = new DashboardScreen();
                    dashboard.show(stage);
                } else {
                    messageLabel.setText("Invalid username or password");
                }
            } catch (Exception ex) {
                messageLabel.setText("Login error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        passwordField.setOnAction(e -> loginButton.fire());

        Scene scene = new Scene(root, 600, 550);
        stage.setScene(scene);
        stage.show();

        usernameField.requestFocus();
    }
}
