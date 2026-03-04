package com.gearrentpro.ui;

import com.gearrentpro.dao.EquipmentCategoryDAO;
import com.gearrentpro.entity.EquipmentCategory;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;

public class CategoryManagementScreen {
    private final EquipmentCategoryDAO categoryDAO;
    private TableView<EquipmentCategory> table;

    public CategoryManagementScreen() {
        this.categoryDAO = new EquipmentCategoryDAO();
    }

    public void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Equipment Categories");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button addButton = new Button("Add Category");
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");

        HBox headerBox = new HBox(20, title, addButton);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        table = new TableView<>();
        table.setStyle("-fx-background-color: white;");

        TableColumn<EquipmentCategory, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<EquipmentCategory, BigDecimal> factorCol = new TableColumn<>("Price Factor");
        factorCol.setCellValueFactory(new PropertyValueFactory<>("basePriceFactor"));
        factorCol.setPrefWidth(100);

        TableColumn<EquipmentCategory, BigDecimal> weekendCol = new TableColumn<>("Weekend Multiplier");
        weekendCol.setCellValueFactory(new PropertyValueFactory<>("weekendMultiplier"));
        weekendCol.setPrefWidth(150);

        TableColumn<EquipmentCategory, BigDecimal> lateFeeCol = new TableColumn<>("Late Fee/Day");
        lateFeeCol.setCellValueFactory(new PropertyValueFactory<>("defaultLateFeePerDay"));
        lateFeeCol.setPrefWidth(120);

        table.getColumns().addAll(nameCol, factorCol, weekendCol, lateFeeCol);

        loadData();

        contentArea.getChildren().addAll(headerBox, table);
        VBox.setMargin(table, new Insets(10, 0, 0, 0));
    }

    private void loadData() {
        try {
            table.getItems().clear();
            table.getItems().addAll(categoryDAO.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading categories: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
