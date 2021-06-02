package com.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class MainWindow extends JFrame {

    BufferedReader br;
    BufferedWriter bw;
    JTextArea writeArea;
    JTextArea readArea;   // currently use text area. however, i can try using table
    JButton sendBtn;
    JButton clsBtn;

    MainWindow(BufferedReader inputStreamReader, BufferedWriter outputStreamWriter, Socket socket) {
        br = inputStreamReader;
        bw = outputStreamWriter;

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
        setVisible(true);
        new Handle().start();    // read the remote msg
    }

    class Handle extends Thread {
        Handle() {

        }

        @Override
        public void run() {
            try {
                String msg = br.readLine();
                readArea.append(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void sendText() throws IOException {
        String str = writeArea.getText();
        if (str.equals("")) {
            JOptionPane.showMessageDialog(null, "您未填写任何信息!");
            return;
        }

        bw.write(str + "\n");
        bw.flush();
    }
}
