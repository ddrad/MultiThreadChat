package com.azarov.homework.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Created by AzarovD on 16.03.2016.
 */
public class Client {

    public static void main(String[] args) throws IOException {

        String serverAddress = JOptionPane.showInputDialog(
                "Enter IP Address of a machine that is running the date service on port 9090:");

        String downloadDirectory = JOptionPane.showInputDialog(
                "Enter directory where you want save file:");

        File directory = new File(downloadDirectory);

        if(!directory.exists() && !directory.isDirectory() ){
            directory.mkdirs();
        }

        Socket s = new Socket(serverAddress, 9090);

        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        int bytesRead;
        int current = 0;
        byte [] mybytearray  = new byte [16*1024];

        bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(downloadDirectory + File.separator + "tmp.doc"));

        InputStream inputStream = s.getInputStream();

        bytesRead = inputStream.read(mybytearray,0,mybytearray.length);
        current = bytesRead;

        do {
            bytesRead = inputStream.read(mybytearray, current, (mybytearray.length-current));
            if(bytesRead >= 0) current += bytesRead;
        } while(bytesRead > -1);

        bufferedOutputStream.write(mybytearray, 0 , current);
        bufferedOutputStream.flush();
        System.out.println("File  downloaded (" + current + " bytes read)");

        JOptionPane.showMessageDialog(null, "File  downloaded (" + current + " bytes read)");
        System.exit(0);
    }
}
