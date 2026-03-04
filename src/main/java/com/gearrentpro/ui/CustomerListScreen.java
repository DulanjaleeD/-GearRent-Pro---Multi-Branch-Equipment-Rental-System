package com.gearrentpro.ui;

import com.gearrentpro.dao.CustomerDAO;
import com.gearrentpro.entity.Customer;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;

public class CustomerListScreen {
    private final CustomerDAO customerDAO;
    private TableView<Customer> table;

    public CustomerListScreen() {
        this.customerDAO = new CustomerDAO();
    }

    public void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Customer Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button addButton = new Button("Add Customer");
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");

        HBox headerBox = new HBox(20, title, addButton);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        table = new TableView<>();
        table.setStyle("-fx-background-color: white;");

        TableColumn<Customer, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("customerCode"));
        codeCol.setPrefWidth(100);

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Customer, String> nicCol = new TableColumn<>("NIC/Passport");
        nicCol.setCellValueFactory(new PropertyValueFactory<>("nicPassport"));
        nicCol.setPrefWidth(120);

        TableColumn<Customer, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        contactCol.setPrefWidth(120);

        TableColumn<Customer, String> membershipCol = new TableColumn<>("Membership");
        membershipCol.setCellValueFactory(new PropertyValueFactory<>("membershipLevelName"));
        membershipCol.setPrefWidth(100);

        TableColumn<Customer, BigDecimal> depositCol = new TableColumn<>("Deposit Held");
        depositCol.setCellValueFactory(new PropertyValueFactory<>("totalDepositHeld"));
        depositCol.setPrefWidth(120);

        table.getColumns().addAll(codeCol, nameCol, nicCol, contactCol, membershipCol, depositCol);

        loadData();

        contentArea.getChildren().addAll(headerBox, table);
        VBox.setMargin(table, new Insets(10, 0, 0, 0));
    }

    private void loadData() {
        try {
            table.getItems().clear();
            table.getItems().addAll(customerDAO.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading customers: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
