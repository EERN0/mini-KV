package ck.top.backend;

import ck.top.backend.server.Server;

public class Launcher {
    public static final int PORT = 6789;

    public static void main(String[] args) {
        Server server = new Server(PORT);
        server.start();
    }
}
