package com.gearrentpro.ui;

import com.gearrentpro.dao.MembershipLevelDAO;
import com.gearrentpro.entity.MembershipLevel;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;

public class MembershipManagementScreen {
    private final MembershipLevelDAO membershipDAO;
    private TableView<MembershipLevel> table;

    public MembershipManagementScreen() {
        this.membershipDAO = new MembershipLevelDAO();
    }

    public void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Membership Levels");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        table = new TableView<>();
        table.setStyle("-fx-background-color: white;");

        TableColumn<MembershipLevel, String> nameCol = new TableColumn<>("Level Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("levelName"));
        nameCol.setPrefWidth(200);

        TableColumn<MembershipLevel, BigDecimal> discountCol = new TableColumn<>("Discount %");
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discountPercentage"));
        discountCol.setPrefWidth(120);

        TableColumn<MembershipLevel, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(300);

        table.getColumns().addAll(nameCol, discountCol, descCol);

        loadData();

        contentArea.getChildren().addAll(title, table);
        VBox.setMargin(table, new Insets(10, 0, 0, 0));
    }

    private void loadData() {
        try {
            table.getItems().clear();
            table.getItems().addAll(membershipDAO.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading membership levels: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
