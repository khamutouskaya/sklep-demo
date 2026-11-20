package com.example.sklep.controller.lobby;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AutoryzacjaController {

    @FXML
    private TextField inputLogin;

    @FXML
    private PasswordField inputPassword;

    @FXML
    private Label labelError;

    public void initialize(){//начальные значения
        labelError.setVisible(false);
    }

    @FXML
    void onBtnLogin(ActionEvent event) {
        labelError.setText("Field must not be empty");//сначала устан значение потом отображаем
        labelError.setVisible(true);


    }

    @FXML
    void onLnkRegistration(ActionEvent event) {

    }

}

