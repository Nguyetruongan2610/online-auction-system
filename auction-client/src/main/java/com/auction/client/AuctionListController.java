package com.auction.client;

import com.auction.common.item.Item;
import com.auction.common.item.ItemFactory;
import com.auction.common.item.ItemType;
import com.auction.common.model.Auction;
import com.auction.common.user.Seller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class AuctionListController {

    @FXML private Label welcomeLabel;
    @FXML private ListView<String> auctionListView;

    @FXML
    public void initialize() {
        Seller seller = new Seller("S01", "Royal Gallery", "seller@auction.com", 4.9);

        Item item1 = ItemFactory.createItem(ItemType.ELECTRONICS, "I01", "MacBook Pro M3 Max 64GB", 2500.0);
        Item item2 = ItemFactory.createItem(ItemType.ART, "I02", "Tranh Sơn Dầu Thế Kỷ XIX", 8000.0);
        Item item3 = ItemFactory.createItem(ItemType.VEHICLE, "I03", "Porsche 911 Carrera", 95000.0);

        Auction a1 = new Auction("AUC-01", item1, seller, item1.getBasePrice());
        Auction a2 = new Auction("AUC-02", item2, seller, item2.getBasePrice());
        Auction a3 = new Auction("AUC-03", item3, seller, item3.getBasePrice());

        auctionListView.setItems(FXCollections.observableArrayList(
                formatAuction(a1),
                formatAuction(a2),
                formatAuction(a3)
        ));
    }

    private String formatAuction(Auction a) {
        return String.format("[%s] %s (%s) | Người bán: %s | Giá hiện tại: $%.0f | Trạng thái: %s",
                a.getAuctionId(),
                a.getItem().getName(),
                a.getItem().getType(),
                a.getSeller().getName(),
                a.getCurrentPrice(),
                a.getStatus());
    }

    public void setCurrentUser(String username) {
        welcomeLabel.setText("Xin chào: " + username);
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 800));
            stage.setTitle("Online Auction System");
        } catch (Exception ignored) {
        }
    }
}