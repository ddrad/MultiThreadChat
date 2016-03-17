package com.azarov.homework.client;

import java.io.*;
import java.net.Socket;

import javax.swing.*;

/**
 * Created by AzarovD on 16.03.2016.
 */
public class Client {

    public static void main(String[] args) throws IOException {

        String serverAddress = JOptionPane.showInputDialog(
                "Enter IP Address of a machine that is running the date service on port 9090:");

        File directory = getSelectedDirectory();
        String downloadFileName = null;
        Socket socket = new Socket(serverAddress, 9090);

        while (true) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            String answer = bufferedReader.readLine();
            String[] items = answer.split("\\|");
            downloadFileName = (String) JOptionPane.showInputDialog(new JFrame(), "Pick a printer", "Input",
                    JOptionPane.QUESTION_MESSAGE, null, items, null);
            writer.println(downloadFileName);
            break;
        }

        while (true) {
            BufferedOutputStream bufferedOutputStream = null;
            int bytesRead;
            int current = 0;
            InputStream inputStream = socket.getInputStream();

            byte[] mybytearray = new byte[16 * 1024];
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(directory.getAbsolutePath() +
                    File.separator + downloadFileName));
            bytesRead = inputStream.read(mybytearray, 0, mybytearray.length);
            current = bytesRead;
            do {
                bytesRead = inputStream.read(mybytearray, current, (mybytearray.length - current));
                if (bytesRead >= 0) current += bytesRead;
            } while (bytesRead > -1);
            bufferedOutputStream.write(mybytearray, 0, current);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            System.out.println("File  downloaded (" + current + " bytes read)");
            JOptionPane.showMessageDialog(null, "File  downloaded (" + current + " bytes read)");
            break;
        }

        System.exit(0);
    }

    private static File getSelectedDirectory() {
        JFileChooser chooser = createJFileChooser();
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("Selected : " + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
        return chooser.getSelectedFile();
    }

    private static JFileChooser createJFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        chooser.setDialogTitle("Enter directory where you want save file:");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser;
    }
}
