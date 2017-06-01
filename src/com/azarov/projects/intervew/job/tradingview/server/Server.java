package com.azarov.projects.intervew.job.tradingview.server;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by AzarovD on 16.03.2016.
 */

public class Server {

    private static final File USER_HOME = new File(System.getProperty("user.home"));

    private void StartServer() throws IOException {

        ServerSocket listener = new ServerSocket(9090);
        File store = getSelectedDirectory();
        StringBuffer filesName = getAllFilesInDirectory(store);

        try {
            while (true) {
                new Handler(listener.accept(), store, filesName).start();
            }
        } finally {
            listener.close();
        }
    }

    private File getSelectedDirectory() {
        JFileChooser chooser = createJFileChooser();

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("Store : " + chooser.getSelectedFile());
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "You didn't select upload directory\n" +
                    "Default directory will 'user home'");
            return USER_HOME;
        }
        return chooser.getSelectedFile();
    }

    private JFileChooser createJFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(USER_HOME);
        chooser.setDialogTitle("Enter directory from whence you want upload file:");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser;
    }

    private StringBuffer getAllFilesInDirectory(File store) {
        StringBuffer stringBuffer = new StringBuffer();
        File[] files = store.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });

        for (File file : files) {
            stringBuffer.append(file.getName()).append(" ( ").append(file.length()).append(" )").append("|");
        }
        return stringBuffer;
    }

    private class Handler extends Thread {

        private Socket socket;
        private File store;
        private StringBuffer filesName;

        public Handler(Socket socket, File store, StringBuffer filesName) {
            this.socket = socket;
            this.store = store;
            this.filesName = filesName;
        }

        public void run() {
            BufferedInputStream bufferedInputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            PrintWriter writer = null;
            BufferedReader reader = null;
            byte[] byteArray;
            final String fileName;

            try {

                while (true) {
                    writer = new PrintWriter(socket.getOutputStream(), true);
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer.println(filesName);
                    fileName = reader.readLine();

                    if (fileName == null) {
                        return;
                    }
                    break;
                }

                while (true) {

                    bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());

                    File[] files = store.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.equals(fileName);
                        }
                    });

                    bufferedInputStream = new BufferedInputStream(new FileInputStream(files[0]));
                    byteArray = new byte[(int) files[0].length()];
                    bufferedInputStream.read(byteArray, 0, byteArray.length);
                    System.out.println("Sending " + files[0].getName() + "( size: " + byteArray.length + " bytes)");
                    bufferedOutputStream.write(byteArray, 0, byteArray.length);
                    bufferedOutputStream.flush();
                    System.out.println("Done.");
                    break;
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (bufferedInputStream != null) bufferedInputStream.close();
                    if (bufferedOutputStream != null) bufferedOutputStream.close();
                    if (writer != null) writer.close();
                    if (reader != null) reader.close();
                    if (socket != null) socket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().StartServer();
    }
}

