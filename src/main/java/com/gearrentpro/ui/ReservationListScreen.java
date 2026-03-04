package com.gearrentpro.ui;

import com.gearrentpro.dao.ReservationDAO;
import com.gearrentpro.entity.Reservation;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;

public class ReservationListScreen {
    private final ReservationDAO reservationDAO;
    private final Integer branchId;
    private TableView<Reservation> table;

    public ReservationListScreen(Integer branchId) {
        this.reservationDAO = new ReservationDAO();
        this.branchId = branchId;
    }

    public void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Reservations");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button addButton = new Button("New Reservation");
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");

        HBox headerBox = new HBox(20, title, addButton);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        table = new TableView<>();
        table.setStyle("-fx-background-color: white;");

        TableColumn<Reservation, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("reservationCode"));
        codeCol.setPrefWidth(130);

        TableColumn<Reservation, String> equipmentCol = new TableColumn<>("Equipment");
        equipmentCol.setCellValueFactory(new PropertyValueFactory<>("equipmentName"));
        equipmentCol.setPrefWidth(200);

        TableColumn<Reservation, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerCol.setPrefWidth(150);

        TableColumn<Reservation, LocalDate> startCol = new TableColumn<>("Start Date");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startCol.setPrefWidth(100);

        TableColumn<Reservation, LocalDate> endCol = new TableColumn<>("End Date");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endCol.setPrefWidth(100);

        TableColumn<Reservation, Reservation.ReservationStatus> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(codeCol, equipmentCol, customerCol, startCol, endCol, statusCol);

        loadData();

        contentArea.getChildren().addAll(headerBox, table);
        VBox.setMargin(table, new Insets(10, 0, 0, 0));
    }

    private void loadData() {
        try {
            table.getItems().clear();
            if (branchId != null) {
                table.getItems().addAll(reservationDAO.findByBranch(branchId));
            } else {
                table.getItems().addAll(reservationDAO.findAll());
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading reservations: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
