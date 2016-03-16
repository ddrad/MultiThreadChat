package com.azarov.homework.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            System.out.println(socket.toString());
            PrintWriter out = null;
            File file = new File("D:\\Workspace\\Projects\\TradeView\\Task_Java_2.doc");
            FileInputStream fileInputStream = null;
            BufferedInputStream bufferedInputStream = null;
            OutputStream outputStream = null;

            BufferedOutputStream bufferedOutputStream = null;

            try {
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                byte [] byteArray  = new byte [(int)file.length()];

                bufferedInputStream.read(byteArray,0,byteArray.length); // copied file into byteArray

                //sending file through socket
                bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                System.out.println("Sending " + file.getName() + "( size: " + byteArray.length + " bytes)");
                bufferedOutputStream.write(byteArray,0,byteArray.length);
                bufferedOutputStream.flush();
                System.out.println("Done.");

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            finally {
                try {
                    bufferedInputStream.close();
                    bufferedOutputStream.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

}

