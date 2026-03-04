package com.gearrentpro.ui;

import com.gearrentpro.dao.EquipmentDAO;
import com.gearrentpro.entity.Equipment;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;

public class EquipmentListScreen {
    private final EquipmentDAO equipmentDAO;
    private final Integer branchId;
    private TableView<Equipment> table;

    public EquipmentListScreen(Integer branchId) {
        this.equipmentDAO = new EquipmentDAO();
        this.branchId = branchId;
    }

    public void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Equipment Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button addButton = new Button("Add Equipment");
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");

        HBox headerBox = new HBox(20, title, addButton);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        table = new TableView<>();
        table.setStyle("-fx-background-color: white;");

        TableColumn<Equipment, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("equipmentCode"));
        codeCol.setPrefWidth(120);

        TableColumn<Equipment, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        categoryCol.setPrefWidth(130);

        TableColumn<Equipment, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        brandCol.setPrefWidth(100);

        TableColumn<Equipment, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setPrefWidth(150);

        TableColumn<Equipment, BigDecimal> priceCol = new TableColumn<>("Daily Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("baseDailyPrice"));
        priceCol.setPrefWidth(100);

        TableColumn<Equipment, Equipment.EquipmentStatus> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(120);

        TableColumn<Equipment, String> branchCol = new TableColumn<>("Branch");
        branchCol.setCellValueFactory(new PropertyValueFactory<>("branchName"));
        branchCol.setPrefWidth(130);

        table.getColumns().addAll(codeCol, categoryCol, brandCol, modelCol, priceCol, statusCol, branchCol);

        loadData();

        contentArea.getChildren().addAll(headerBox, table);
        VBox.setMargin(table, new Insets(10, 0, 0, 0));
    }

    private void loadData() {
        try {
            table.getItems().clear();
            if (branchId != null) {
                table.getItems().addAll(equipmentDAO.findByBranch(branchId));
            } else {
                table.getItems().addAll(equipmentDAO.findAll());
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading equipment: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
