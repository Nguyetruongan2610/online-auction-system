package com.auction.server.demo;

public class RaceConditionDemo {
    private static int currentBidPriceUnsafe = 0;
    private static int currentBidPriceSafe = 0;

    // Hàm đặt giá KHÔNG an toàn luồng (dễ bị race condition)
    public static void placeBidUnsafe(int amount) {
        int newPrice = currentBidPriceUnsafe + amount;
        try {
            Thread.sleep(1); // Giả lập độ trễ xử lý
        } catch (InterruptedException ignored) {}
        currentBidPriceUnsafe = newPrice;
    }

    // Hàm đặt giá AN TOÀN luồng (dùng synchronized)
    public static synchronized void placeBidSafe(int amount) {
        int newPrice = currentBidPriceSafe + amount;
        try {
            Thread.sleep(1);
        } catch (InterruptedException ignored) {}
        currentBidPriceSafe = newPrice;
    }

    public static void main(String[] args) throws InterruptedException {
        int numberOfThreads = 100;
        int increment = 10;
        int expectedTotal = numberOfThreads * increment; // Giá kỳ vọng: 1000

        // 1. Thử nghiệm khi KHÔNG dùng synchronized
        Thread[] threadsUnsafe = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threadsUnsafe[i] = new Thread(() -> placeBidUnsafe(increment));
            threadsUnsafe[i].start();
        }
        for (Thread t : threadsUnsafe) t.join();

        System.out.println("=== KẾT QUẢ MÔ PHỎNG RACE CONDITION ===");
        System.out.println("Kỳ vọng giá cuối cùng: " + expectedTotal);
        System.out.println("Thực tế KHÔNG có synchronized: " + currentBidPriceUnsafe + " (Sai giá do xung đột luồng)");

        // 2. Thử nghiệm khi CÓ synchronized
        Thread[] threadsSafe = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threadsSafe[i] = new Thread(() -> placeBidSafe(increment));
            threadsSafe[i].start();
        }
        for (Thread t : threadsSafe) t.join();

        System.out.println("Thực tế CÓ synchronized: " + currentBidPriceSafe + " (Đúng giá chuẩn xác)");
    }
}