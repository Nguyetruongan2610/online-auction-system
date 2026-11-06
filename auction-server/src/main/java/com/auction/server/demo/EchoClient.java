package com.auction.server.demo;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class EchoClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Đã kết nối tới Server. Nhập tin nhắn (nhập 'quit' để thoát):");
            while (true) {
                System.out.print("> ");
                String userInput = scanner.nextLine();
                out.println(userInput);

                String response = in.readLine();
                System.out.println("Server phản hồi: " + response);

                if ("quit".equalsIgnoreCase(userInput.trim())) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi kết nối Server: " + e.getMessage());
        }
    }
}