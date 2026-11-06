package com.auction.server.demo;

import java.io.*;
import java.net.*;

public class EchoServer {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("Server đang chạy trên port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client mới kết nối: " + clientSocket.getInetAddress());
                // Tạo thread mới cho mỗi client để xử lý đa luồng
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if ("quit".equalsIgnoreCase(inputLine.trim())) {
                        out.println("Bye!");
                        break;
                    }
                    System.out.println("Nhận từ client: " + inputLine);
                    out.println("Echo: " + inputLine);
                }
            } catch (IOException e) {
                System.err.println("Lỗi kết nối client: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}