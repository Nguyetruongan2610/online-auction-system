package com.auction.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Vui lòng nhập đầy đủ Email/Username và Mật khẩu!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/auction-list.fxml"));
            Parent root = loader.load();

            AuctionListController controller = loader.getController();
            controller.setCurrentUser(email);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 800));
            stage.setTitle("Online Auction System - Danh sách đấu giá");
        } catch (Exception e) {
            statusLabel.setText("Lỗi chuyển màn hình: " + e.getMessage());
        }
    }

    @FXML
    private void handleForgotPassword() {
        statusLabel.setText("Tính năng khôi phục mật khẩu sẽ cập nhật ở phiên bản sau.");
    }

    @FXML
    private void handleCreateAccount() {
        statusLabel.setText("Tính năng đăng ký tài khoản sẽ cập nhật ở phiên bản sau.");
    }
}
