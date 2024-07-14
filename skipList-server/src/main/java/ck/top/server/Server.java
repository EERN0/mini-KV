package ck.top.server;

import ck.top.skipList.SkipList;

import java.io.*;
import java.net.*;

public class Server {
    private final SkipList<String, String> skipList;
    private final int port;
    ServerSocket socket = null;
    Socket clientSocket = null;

    public Server(int port) {
        this.port = port;
        skipList = SkipList.getInstance();
    }

    public void start() {

        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Server started on port: " + port);


        while (true) {

            try {
                clientSocket = socket.accept();

                HandlerSocket worker = new HandlerSocket(clientSocket);
                worker.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    private class HandlerSocket extends Thread {

        private final Socket socket;

        public HandlerSocket(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            InetSocketAddress address = (InetSocketAddress) socket.getRemoteSocketAddress();
            System.out.println("Establish connection: " + address.getAddress().getHostAddress() + " : " + address.getPort());
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String command;
                while ((command = in.readLine()) != null) {
                    String[] commandList = command.split(" ");
                    switch (commandList[0]) {
                        case "insert": {
                            boolean b = skipList.insert(commandList[1], commandList[2]);
                            if (b) {
                                out.println("key: " + commandList[1] + " value: " + commandList[2] + " insert success!");
                            } else {
                                out.println("key: " + commandList[1] + " value: " + commandList[2] + " insert failed");
                            }
                            break;
                        }
                        case "remove": {
                            boolean b = skipList.remove(commandList[1]);
                            if (b) {
                                out.println("key: " + commandList[1] + " deleted!");
                            } else {
                                out.println("skiplist not exists the key: " + commandList[1]);
                            }
                            break;
                        }
                        case "search": {
                            boolean b = skipList.isExist(commandList[1]);
                            if (b) {
                                out.println("key: " + commandList[1] + " searched!");
                            } else {
                                out.println("key: " + commandList[1] + " not exists!");
                            }
                            break;
                        }
                        case "get": {
                            if (!skipList.isExist(commandList[1])) {
                                out.println("key: " + commandList[1] + " not exists!");
                            }
                            String node = skipList.get(commandList[1]);
                            if (node != null) {
                                out.println("key: " + commandList[1] + "'s value is " + node);
                            }
                            break;
                        }
                        case "flush":
                            skipList.flush();
                            out.println("Already saved skiplist.");
                            break;
                        case "load":
                            skipList.load();
                            break;
                        default:
                            skipList.show();
                            break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}