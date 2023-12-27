package org.kpi.server;

import org.kpi.utils.CustomThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int N_THREADS = 10; // Кількість потоків у пулі
    private static final CustomThreadPool threadPool = new CustomThreadPool(N_THREADS);

    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущено на порту " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новий клієнт приєднався!");

                // Замість створення нового потоку, використовуємо тред пул
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



