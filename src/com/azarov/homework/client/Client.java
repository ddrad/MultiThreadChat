package com.azarov.homework.client;

import java.io.*;
import java.net.Socket;

import javax.swing.*;

/**
 * Created by AzarovD on 16.03.2016.
 */
public class Client {

    private static final File USER_HOME = new File(System.getProperty("user.home"));
    private static final String DEFAULT_HOST = "localhost";

    public void start() throws IOException {

        String serverAddress = getServerAddress();
        File directory = getSelectedDirectory();
        String downloadFileName;
        byte[] fileSize;
        Socket socket = new Socket(serverAddress, 9090);

        while (true) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            String answer = bufferedReader.readLine();
            String[] items = answer.split("\\|");
            String item = (String) JOptionPane.showInputDialog(new JFrame(), "Pick a printer", "Input",
                    JOptionPane.QUESTION_MESSAGE, null, items, null);
            String[] attr = item.split("\\s");
            fileSize = new byte[Integer.parseInt(attr[2]) + 1];
            downloadFileName = attr[0];
            writer.println(downloadFileName);
            break;
        }

        while (true) {
            BufferedOutputStream bufferedOutputStream = null;
            int bytesRead;
            int current;
            InputStream inputStream = socket.getInputStream();
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(directory.getAbsolutePath() +
                    File.separator + downloadFileName));
            bytesRead = inputStream.read(fileSize, 0, fileSize.length);
            current = bytesRead;
            do {
                bytesRead = inputStream.read(fileSize, current, (fileSize.length - current));
                if (bytesRead >= 0) current += bytesRead;
            } while (bytesRead > -1);
            bufferedOutputStream.write(fileSize, 0, current);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            System.out.println("File  downloaded (" + current + " bytes read)");
            JOptionPane.showMessageDialog(null, "File  downloaded (" + current + " bytes read)");
            break;
        }
        System.exit(0);
    }

    private String getServerAddress() {
        String serverAddress = JOptionPane.showInputDialog(
                "Port 9090.\n Enter server IP Address without port (e.g localhost or 127.0.0.1):");

        if (serverAddress == null || serverAddress.equals("")) {
            serverAddress = DEFAULT_HOST;
            JOptionPane.showMessageDialog(new JFrame(), "You not selected server adrress.\n"
                    + "Default value 'localhost'");
        }
        return serverAddress;
    }

    private File getSelectedDirectory() {
        JFileChooser chooser = createJFileChooser();
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("Selected : " + chooser.getSelectedFile());
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "You not selected upload directory.\n" +
                    "Default directory 'user home'");
            return USER_HOME;
        }
        return chooser.getSelectedFile();
    }

    private JFileChooser createJFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(USER_HOME);
        chooser.setDialogTitle("Enter directory where you want save file:");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser;
    }

    public static void main(String[] args) throws IOException {
        new Client().start();
    }
}
