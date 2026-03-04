package com.gearrentpro.ui;

import com.gearrentpro.dao.RentalDAO;
import com.gearrentpro.entity.Rental;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalListScreen {
    private final RentalDAO rentalDAO;
    private final Integer branchId;
    private TableView<Rental> table;

    public RentalListScreen(Integer branchId) {
        this.rentalDAO = new RentalDAO();
        this.branchId = branchId;
    }

    public void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Rentals");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");
        refreshButton.setOnAction(e -> loadData());

        HBox headerBox = new HBox(20, title, refreshButton);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        table = new TableView<>();
        table.setStyle("-fx-background-color: white;");

        TableColumn<Rental, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("rentalCode"));
        codeCol.setPrefWidth(130);

        TableColumn<Rental, String> equipmentCol = new TableColumn<>("Equipment");
        equipmentCol.setCellValueFactory(new PropertyValueFactory<>("equipmentName"));
        equipmentCol.setPrefWidth(180);

        TableColumn<Rental, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerCol.setPrefWidth(130);

        TableColumn<Rental, LocalDate> startCol = new TableColumn<>("Start");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startCol.setPrefWidth(90);

        TableColumn<Rental, LocalDate> endCol = new TableColumn<>("End");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endCol.setPrefWidth(90);

        TableColumn<Rental, BigDecimal> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("finalRentalAmount"));
        amountCol.setPrefWidth(90);

        TableColumn<Rental, Rental.RentalStatus> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("rentalStatus"));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(codeCol, equipmentCol, customerCol, startCol, endCol, amountCol, statusCol);

        loadData();

        contentArea.getChildren().addAll(headerBox, table);
        VBox.setMargin(table, new Insets(10, 0, 0, 0));
    }

    private void loadData() {
        try {
            table.getItems().clear();
            if (branchId != null) {
                table.getItems().addAll(rentalDAO.findByBranch(branchId));
            } else {
                table.getItems().addAll(rentalDAO.findAll());
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading rentals: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
