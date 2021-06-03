package com.Yuxi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MainWindow extends JFrame {

    private JTextArea writeArea;
    private JTextArea readArea;   // currently use text area. however, i can try using table
    private JButton sendBtn;
    private JButton clsBtn;
    private String user_name;
    private String passwd;

    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    MainWindow(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream,
               Socket socket, String _user_name, String _passwd) {

        user_name = _user_name;
        passwd = _passwd;
        in = objectInputStream;
        out = objectOutputStream;

        writeArea = new JTextArea(15, 20);
        readArea = new JTextArea(15, 20);
        writeArea.setLineWrap(true);
        readArea.setLineWrap(true);
        jScrollPane1 = new JScrollPane(readArea);
        jScrollPane2 = new JScrollPane(writeArea);
        jScrollPane1.setSize(getWidth(), getHeight());
        jScrollPane2.setSize(getWidth(), getHeight());
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
        setLayout(new FlowLayout());

        add(jScrollPane2);
        add(jScrollPane1);
        add(sendBtn);
        add(clsBtn);

        setLayout(null);

        jScrollPane1.setBounds(0, 0, 400, 150);
        jScrollPane2.setBounds(0, 160, 400, 150);
        sendBtn.setBounds(80, 320, 100, 30);
        clsBtn.setBounds(200, 320, 100, 30);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
        new Handle().start();    // read the remote msg
    }

    class Handle extends Thread {

        User info;
        int index;
        byte[] data;
        int totalSize;
        int type;
        int dataSize = 0;
        String users = "";
        String infoString = "";
        String user;

        @Override
        public void run() {
            while (true) {
                try {
                    info = (User) in.readObject();
                    index = info.getIndex();
                    data = info.getData();
                    users = info.getGroup();
                    totalSize = info.getTotal_size();
                    type = info.getType();
                    dataSize += info.getData_size();
                    user = info.getUser_name();

                    if (dataSize == totalSize) {
                        if (type == 1) {
                            System.out.println(Arrays.toString(data));
                            infoString += new String(data);
                            infoString = user + ":  " + infoString;
                            getTText(infoString);
                            infoString = "";
                            data = null;
                        } else {
                            /**
                             *
                             */
                        }
                        dataSize = 0;
                        users = "";
                        info = null;
                    } else {
                        if (type == 1) {
                            System.out.println(Arrays.toString(data));
                            infoString += new String(data);
                        } else {
                            /**
                             *
                             */
                        }
                    }
                } catch (EOFException | SocketException e) {
                    JOptionPane.showConfirmDialog(null, "连接出现问题，即将退出");
                    System.exit(-1);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    JOptionPane.showConfirmDialog(null, "连接出现问题，即将退出");
                    System.exit(-1);
                }
            }


        }
    }

    void getTText(String info) {
        readArea.append(info + "\n");
    }

    void sendText() throws IOException {
        String str = writeArea.getText();
        if (str.equals("")) {
            JOptionPane.showMessageDialog(null, "您未填写任何信息!");
            return;
        }

        out.writeObject(new User(user_name, passwd, 1, 1, str.length(), "asd",
                str.getBytes(StandardCharsets.UTF_8), str.length()));
        out.flush();
    }
}
