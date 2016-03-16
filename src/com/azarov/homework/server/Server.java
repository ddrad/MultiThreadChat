package com.azarov.homework.server;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by AzarovD on 16.03.2016.
 */
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket listener = new ServerSocket(9090);

        File store = getSelectedDirectory();

        try {
            while (true) {

                try {
                    while (true) {
                        new Handler(listener.accept(), store).start();
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

    private static File getSelectedDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle("choosertitle");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }

        return chooser.getSelectedFile();
    }

    private static class Handler extends Thread {

        private Socket socket;

        public Handler(Socket socket, File store) {
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

