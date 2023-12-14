package org.kpi.server;

import org.kpi.index.Index;
import org.kpi.utils.FileUtils;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Index index;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            index = Index.getInstance();
            if(!index.isIndexed()){
                writer.println("Інвертований індекс не містить даних. Введіть кількість потоків для обробки: ");

                try{
                    int threadNum = Integer.parseInt(reader.readLine());

                    File mainDir = new File("files");
                    List<File> files = FileUtils.getListOfFiles(mainDir);

                    long time = Index.getInstance().index(threadNum, files);

                    writer.println("Проіндексовано, витрачено: " + time + "мс.");
                    writer.println("Введіть слово для пошуку, або q щоб вийти");
                }
                catch (Exception e){
                    e.printStackTrace();
                    writer.println("Некоректні дані, спробуйте ще раз.");
                }
            }
            else {
                writer.println("Структуру даних вже проіндексовано. Введіть слово для пошуку.");
            }
            String clientMessage;
            while (true) {
                clientMessage = reader.readLine();
                System.out.println("Отримано від клієнта: " + clientMessage);
                writer.println(index.find(clientMessage));

                writer.println("Введіть слово для пошуку, або q щоб вийти");
                if(clientMessage.equals("q")){
                    clientSocket.close();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}