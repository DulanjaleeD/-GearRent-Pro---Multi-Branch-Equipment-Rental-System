package com.gearrentpro.ui;

import com.gearrentpro.dao.BranchDAO;
import com.gearrentpro.entity.Branch;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BranchManagementScreen {
    private final BranchDAO branchDAO;
    private TableView<Branch> table;

    public BranchManagementScreen() {
        this.branchDAO = new BranchDAO();
    }

    public void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Branch Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button addButton = new Button("Add New Branch");
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");

        HBox headerBox = new HBox(20, title, addButton);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        table = new TableView<>();
        table.setStyle("-fx-background-color: white;");

        TableColumn<Branch, String> codeCol = new TableColumn<>("Branch Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("branchCode"));
        codeCol.setPrefWidth(120);

        TableColumn<Branch, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<Branch, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.setPrefWidth(250);

        TableColumn<Branch, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        contactCol.setPrefWidth(120);

        TableColumn<Branch, Boolean> activeCol = new TableColumn<>("Active");
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        activeCol.setPrefWidth(80);

        table.getColumns().addAll(codeCol, nameCol, addressCol, contactCol, activeCol);

        loadData();

        contentArea.getChildren().addAll(headerBox, table);
        VBox.setMargin(table, new Insets(10, 0, 0, 0));
    }

    private void loadData() {
        try {
            table.getItems().clear();
            table.getItems().addAll(branchDAO.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading branches: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
