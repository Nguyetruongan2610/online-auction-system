
package com.auction.client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter your email and password!");
            return;
        }

        // TODO: Connect to the authentication service
        statusLabel.setText("Login form is ready. Authentication is not connected yet.");
    }

    @FXML
    private void handleForgotPassword() {
        statusLabel.setText("Password recovery is not implemented yet.");
    }

    @FXML
    private void handleCreateAccount() {
        statusLabel.setText("Registration is not implemented yet.");
    }
}
