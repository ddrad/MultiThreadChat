package com.azarov.homework.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by AzarovD on 16.03.2016.
 */
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket listener = new ServerSocket(9090);

        try {
            while (true) {

                try {
                    while (true) {
                        new Handler(listener.accept()).start();
                    }
                } finally {
                    listener.close();
                }
            }
        }
        finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {

        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            System.out.println("");
            PrintWriter out = null;
            try {
                while (true) {
                    out = new PrintWriter(socket.getOutputStream(), true);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            out.println(new Date().toString());
        }
    }

}

