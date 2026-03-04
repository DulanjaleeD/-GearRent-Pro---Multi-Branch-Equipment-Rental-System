package com.gearrentpro.ui;

import com.gearrentpro.entity.User;
import com.gearrentpro.service.AuthService;
import com.gearrentpro.util.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardScreen {
    private final AuthService authService;
    private final SessionManager sessionManager;
    private BorderPane mainLayout;
    private VBox contentArea;

    public DashboardScreen() {
        this.authService = new AuthService();
        this.sessionManager = SessionManager.getInstance();
    }

    public void show(Stage stage) {
        User currentUser = sessionManager.getCurrentUser();
        if (currentUser == null) {
            new LoginScreen().show(stage);
            return;
        }

        stage.setTitle("GearRent Pro - Dashboard");

        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #ecf0f1;");

        VBox sidebar = createSidebar(stage, currentUser);
        mainLayout.setLeft(sidebar);

        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(30));
        contentArea.setAlignment(Pos.TOP_LEFT);

        showWelcomeScreen(currentUser);

        mainLayout.setCenter(contentArea);

        Scene scene = new Scene(mainLayout, 1200, 700);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createSidebar(Stage stage, User user) {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #2c3e50; -fx-padding: 20;");

        Label appTitle = new Label("GearRent Pro");
        appTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        appTitle.setStyle("-fx-text-fill: white;");

        Label userInfo = new Label(user.getFullName() + "\n" + user.getRoleName());
        userInfo.setFont(Font.font("Arial", 12));
        userInfo.setStyle("-fx-text-fill: #ecf0f1;");
        userInfo.setWrapText(true);

        if (user.getBranchName() != null) {
            Label branchInfo = new Label("Branch: " + user.getBranchName());
            branchInfo.setFont(Font.font("Arial", 11));
            branchInfo.setStyle("-fx-text-fill: #bdc3c7;");
            branchInfo.setWrapText(true);
            sidebar.getChildren().add(branchInfo);
        }

        Separator separator1 = new Separator();
        separator1.setStyle("-fx-background-color: #34495e;");

        VBox menuBox = new VBox(5);

        if (user.isAdmin()) {
            menuBox.getChildren().addAll(
                    createMenuButton("Branches", () -> new BranchManagementScreen().show(contentArea)),
                    createMenuButton("Equipment Categories", () -> new CategoryManagementScreen().show(contentArea)),
                    createMenuButton("Membership Levels", () -> new MembershipManagementScreen().show(contentArea)),
                    createMenuButton("View All Rentals", () -> new RentalListScreen(null).show(contentArea)),
                    createMenuButton("View All Reservations", () -> new ReservationListScreen(null).show(contentArea)),
                    createMenuButton("Overdue Rentals", () -> new OverdueRentalsScreen(null).show(contentArea))
            );
        } else if (user.isBranchManager() || user.isStaff()) {
            menuBox.getChildren().addAll(
                    createMenuButton("Equipment", () -> new EquipmentListScreen(user.getBranchId()).show(contentArea)),
                    createMenuButton("Customers", () -> new CustomerListScreen().show(contentArea)),
                    createMenuButton("Reservations", () -> new ReservationListScreen(user.getBranchId()).show(contentArea)),
                    createMenuButton("Rentals", () -> new RentalListScreen(user.getBranchId()).show(contentArea)),
                    createMenuButton("Process Return", () -> new ReturnProcessScreen().show(contentArea)),
                    createMenuButton("Overdue Rentals", () -> new OverdueRentalsScreen(user.getBranchId()).show(contentArea))
            );
        }

        Separator separator2 = new Separator();
        separator2.setStyle("-fx-background-color: #34495e;");

        Button logoutButton = new Button("Logout");
        logoutButton.setPrefWidth(210);
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;"));
        logoutButton.setOnAction(e -> {
            authService.logout();
            new LoginScreen().show(stage);
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(appTitle, userInfo, separator1, menuBox, spacer, separator2, logoutButton);
        return sidebar;
    }

    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setPrefWidth(210);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-font-size: 13; -fx-padding: 10;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 13; -fx-padding: 10;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-font-size: 13; -fx-padding: 10;"));
        button.setOnAction(e -> action.run());
        return button;
    }

    private void showWelcomeScreen(User user) {
        contentArea.getChildren().clear();

        Label welcomeLabel = new Label("Welcome, " + user.getFullName());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        welcomeLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label roleLabel = new Label("Role: " + user.getRoleName());
        roleLabel.setFont(Font.font("Arial", 16));
        roleLabel.setStyle("-fx-text-fill: #7f8c8d;");

        VBox infoBox = new VBox(15);
        infoBox.setPadding(new Insets(30));
        infoBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        infoBox.setMaxWidth(700);

        Label instructionTitle = new Label("Getting Started");
        instructionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        instructionTitle.setStyle("-fx-text-fill: #2c3e50;");

        Label instructions = new Label();
        instructions.setWrapText(true);
        instructions.setFont(Font.font("Arial", 13));
        instructions.setStyle("-fx-text-fill: #34495e;");

        if (user.isAdmin()) {
            instructions.setText(
                    "As an Administrator, you have full access to:\n\n" +
                    "• Manage branches across the organization\n" +
                    "• Configure equipment categories and pricing\n" +
                    "• Set up membership levels and discounts\n" +
                    "• View and monitor all rentals and reservations\n" +
                    "• Track overdue rentals system-wide\n\n" +
                    "Use the menu on the left to navigate through different modules."
            );
        } else if (user.isBranchManager()) {
            instructions.setText(
                    "As a Branch Manager, you can:\n\n" +
                    "• Manage equipment inventory for your branch\n" +
                    "• Handle customer registrations and updates\n" +
                    "• Create and manage reservations\n" +
                    "• Process rentals and returns\n" +
                    "• Monitor overdue rentals for your branch\n" +
                    "• Generate branch reports\n\n" +
                    "Use the menu on the left to access these features."
            );
        } else {
            instructions.setText(
                    "As Staff, you can:\n\n" +
                    "• View and manage equipment for your branch\n" +
                    "• Register new customers\n" +
                    "• Create reservations for customers\n" +
                    "• Process rental transactions\n" +
                    "• Handle equipment returns\n" +
                    "• View overdue rentals\n\n" +
                    "Use the menu on the left to access these features."
            );
        }

        infoBox.getChildren().addAll(instructionTitle, instructions);

        contentArea.getChildren().addAll(welcomeLabel, roleLabel, infoBox);
    }
}
