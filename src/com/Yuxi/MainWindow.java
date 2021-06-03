package com.Yuxi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainWindow extends JFrame {

    JTextArea writeArea;
    JTextArea readArea;   // currently use text area. however, i can try using table
    JButton sendBtn;
    JButton clsBtn;
    String user_name;
    String passwd;

    ObjectInputStream in;
    ObjectOutputStream out;

    MainWindow(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, Socket socket, String _user_name, String _passwd) {

        user_name = _user_name;
        passwd = _passwd;
        in = objectInputStream;
        out = objectOutputStream;

        writeArea = new JTextArea(4, 4);
        readArea = new JTextArea(4, 4);
        readArea.setEditable(false);
        sendBtn = new JButton("发送");
        clsBtn = new JButton("清空");

        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendText();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        clsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeArea.setText("");
            }
        });

        add(readArea);
        add(writeArea);
        add(sendBtn);
        add(clsBtn);


        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        System.out.println("true");
        setVisible(true);
        new Handle().start();    // read the remote msg
    }

    class Handle extends Thread {
        Handle() {

        }

        @Override
        public void run() {

            try {
                User info = (User) in.readObject();
                int index = info.getIndex();
                byte[] data = info.getData();
                String users = info.getGroup();
                int totalSize = info.getTotal_size();
                int type = info.getType();
                int dataSize = info.getData_size();


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }


//            try {
//                String msg = br.readLine();
//                readArea.append(msg);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
    }

    void sendText() throws IOException {
        String str = writeArea.getText();
        if (str.equals("")) {
            JOptionPane.showMessageDialog(null, "您未填写任何信息!");
            return;
        }

        if (str.length() <= 65535) {
            out.writeObject(new User(user_name, passwd, 1, 1, str.length(), "asd", str.getBytes(StandardCharsets.UTF_8), str.length()));
            out.flush();
        } else {
            //---
        }
    }
}
