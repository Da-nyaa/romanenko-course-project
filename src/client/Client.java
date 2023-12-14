package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 8080;

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Підключено до сервера " + serverAddress + ":" + serverPort);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> readDataFromServer(reader)).start();

            sendMessagesToServer(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessagesToServer(PrintWriter writer) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String message = scanner.nextLine();
                writer.println(message);
                if ("q".equalsIgnoreCase(message)) {
                    break;
                }
            }
        }
    }

    private static void readDataFromServer(BufferedReader reader) {
            try {
                String serverMessage;
                while ((serverMessage = reader.readLine()) != null) {
                    System.out.println("Отримано від сервера: " + serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}

