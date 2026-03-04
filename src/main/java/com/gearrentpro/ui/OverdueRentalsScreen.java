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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class OverdueRentalsScreen {
    private final RentalDAO rentalDAO;
    private final Integer branchId;
    private TableView<Rental> table;

    public OverdueRentalsScreen(Integer branchId) {
        this.rentalDAO = new RentalDAO();
        this.branchId = branchId;
    }

    public void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Overdue Rentals");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #e74c3c;");

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");
        refreshButton.setOnAction(e -> loadData());

        HBox headerBox = new HBox(20, title, refreshButton);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        table = new TableView<>();
        table.setStyle("-fx-background-color: white;");

        TableColumn<Rental, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("rentalCode"));
        codeCol.setPrefWidth(120);

        TableColumn<Rental, String> equipmentCol = new TableColumn<>("Equipment");
        equipmentCol.setCellValueFactory(new PropertyValueFactory<>("equipmentName"));
        equipmentCol.setPrefWidth(180);

        TableColumn<Rental, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerCol.setPrefWidth(130);

        TableColumn<Rental, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("customerContact"));
        contactCol.setPrefWidth(120);

        TableColumn<Rental, LocalDate> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        dueCol.setPrefWidth(100);

        TableColumn<Rental, String> daysOverdueCol = new TableColumn<>("Days Overdue");
        daysOverdueCol.setCellFactory(col -> new TableCell<Rental, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    Rental rental = getTableRow().getItem();
                    long days = ChronoUnit.DAYS.between(rental.getEndDate(), LocalDate.now());
                    setText(String.valueOf(days));
                    setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                }
            }
        });
        daysOverdueCol.setPrefWidth(110);

        table.getColumns().addAll(codeCol, equipmentCol, customerCol, contactCol, dueCol, daysOverdueCol);

        loadData();

        contentArea.getChildren().addAll(headerBox, table);
        VBox.setMargin(table, new Insets(10, 0, 0, 0));
    }

    private void loadData() {
        try {
            table.getItems().clear();
            if (branchId != null) {
                table.getItems().addAll(rentalDAO.findOverdueByBranch(branchId));
            } else {
                table.getItems().addAll(rentalDAO.findOverdue());
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading overdue rentals: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
