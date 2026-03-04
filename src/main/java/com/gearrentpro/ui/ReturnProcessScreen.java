package com.gearrentpro.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ReturnProcessScreen {

    public void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Process Return");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Label infoLabel = new Label("Return processing functionality");
        infoLabel.setFont(Font.font("Arial", 14));
        infoLabel.setStyle("-fx-text-fill: #7f8c8d;");

        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(20));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        formBox.setMaxWidth(600);

        Label instructionLabel = new Label("To process a return:\n\n" +
                "1. Search for the active rental by code or customer\n" +
                "2. Verify the equipment condition\n" +
                "3. Record any damages and associated charges\n" +
                "4. Calculate late fees if applicable\n" +
                "5. Process the security deposit refund or additional payment\n" +
                "6. Complete the return transaction");
        instructionLabel.setWrapText(true);
        instructionLabel.setFont(Font.font("Arial", 13));

        formBox.getChildren().add(instructionLabel);

        contentArea.getChildren().addAll(title, infoLabel, formBox);
        VBox.setMargin(formBox, new Insets(10, 0, 0, 0));
    }
}
