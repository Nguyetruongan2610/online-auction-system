# Hệ thống Đấu giá Trực tuyến (Online Auction System) — LTNC 2026

Dự án Bài tập lớn môn **Lập trình nâng cao (HKII 2025–2026)** xây dựng theo kiến trúc **Client–Server (Socket)**, **JavaFX (MVC)** và **Maven Multi-module**.

## 1. Cấu trúc Module
- **`auction-common`**: Chứa các lớp Model OOP (`User`, `Item`, `Auction`, `Bid`, `AuctionStatus`) và `ItemFactory` (Factory Method Pattern).
- **`auction-server`**: Chứa `AuctionManager` (Singleton Pattern, Thread-safe `synchronized`), xử lý đấu giá và demo Socket/Concurrency (`EchoServer`, `EchoClient`, `RaceConditionDemo`).
- **`auction-client`**: Ứng dụng giao diện `JavaFX + FXML + CSS` (Màn hình `Login` và `Auction List`).

## 2. Sơ đồ lớp (Class Diagram - Tuần 6)

```mermaid
classDiagram
    direction TB

    class User {
        <<abstract>>
        #String id
        #String name
        #String email
        +getRole()* String
    }
    class Bidder {
        -double balance
        +getRole() String
    }
    class Seller {
        -double rating
        +getRole() String
    }
    class Admin {
        +getRole() String
    }

    User <|-- Bidder
    User <|-- Seller
    User <|-- Admin

    class Item {
        <<abstract>>
        -String id
        -String name
        -double basePrice
        +getType()* ItemType
    }
    class Electronics {
        +getType() ItemType
    }
    class Art {
        +getType() ItemType
    }
    class Vehicle {
        +getType() ItemType
    }

    Item <|-- Electronics
    Item <|-- Art
    Item <|-- Vehicle

    class ItemFactory {
        +createItem(ItemType, String, String, double)$ Item
    }
    ItemFactory ..> Item : creates

    class Auction {
        -String auctionId
        -Item item
        -Seller seller
        -double startingPrice
        -Bid highestBid
        -AuctionStatus status
        +getCurrentPrice() double
    }
    class Bid {
        -Bidder bidder
        -double amount
        -LocalDateTime timestamp
    }
    class AuctionStatus {
        <<enumeration>>
        OPEN
        RUNNING
        FINISHED
        PAID
        CANCELED
    }
    class AuctionManager {
        -AuctionManager instance$
        -Map~String, Auction~ auctions
        +getInstance()$ AuctionManager
        +addAuction(Auction) void
        +startAuction(String) boolean
        +endAuction(String) boolean
        +placeBid(String, Bid) boolean
    }

    Auction --> Item
    Auction --> Seller
    Auction --> Bid
    Auction --> AuctionStatus
    Bid --> Bidder
    AuctionManager --> Auction : manages
```

## 3. Hướng dẫn chạy dự án
- **Chạy Unit Test (JUnit 5):** `mvn clean test`
- **Chạy Giao diện Client (JavaFX):** Chạy hàm `main` trong `com.auction.client.Launcher`
- **Chạy Demo Đa luồng & Socket (Tuần 6):** Chạy `EchoServer`, `EchoClient` và `RaceConditionDemo` trong `auction-server`.