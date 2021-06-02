package com.Main;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Client {
    Socket socket;

    Client(String url, int port, String user, String passwd) {
        try {
            socket = new Socket(url, port);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "端口被占用,程序即将退出");
            System.exit(0);
        }

        Thread mythread = null;
        try {
            mythread = new MyClient(socket, new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))
                    , new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)), user, passwd);
            mythread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MyClient extends Thread {
    Socket s;
    BufferedReader ob;
    BufferedWriter ow;
    String username;
    String passwd;


    MyClient(Socket ss, BufferedReader inputStreamReader, BufferedWriter outputStreamWriter, String name, String pswd) {
        s = ss;
        ow = outputStreamWriter;
        ob = inputStreamReader;
        username = name;
        passwd = pswd;

        new MainWindow(ob, ow);
    }


    @Override
    public void run() {
        if (s.isClosed()) {
            System.out.println("why");
            return;
        }

        try {
            ow.write(username + "\n" + passwd + "\n");
            ow.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


