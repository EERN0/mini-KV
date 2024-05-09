package ck.top.backend.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int serverPort = 6789;

        try {
            Socket socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to server.");

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String userInputStr;
            while ((userInputStr = userInput.readLine()) != null) {
                out.println(userInputStr); // 将用户输入的命令发送给服务器

                String serverResponse = in.readLine(); // 从服务器读取响应
                System.out.println("Server response: " + serverResponse);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
