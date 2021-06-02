package com.Main;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Client {
    Socket socket;

    BufferedWriter bw;
    BufferedReader br;

    //  有时间把login 和 register 合并
    void register(String user, String passwd, Login login) {
        Thread mythread = null;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            bw.write("register\n");
            bw.flush();

            // validate the username
            bw.write(user + "\n");
            bw.flush();
            bw.write(passwd + "\n");
            bw.flush();

            String str = br.readLine();
            if (str.equals("valid")) {
                login.setVisible(false);
                new MainWindow(br, bw, socket);
            } else {
                JOptionPane.showMessageDialog(null, "用户名重复，请重新注册!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void login(String user, String passwd, Login login) {
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            bw.write("login\n");
            bw.flush();

            // validate the username
            bw.write(user + "\n");
            bw.flush();
            bw.write(passwd + "\n");
            bw.flush();


            String str = br.readLine();
            if (str.equals("invalid name! you should register it first!")) {
                System.out.println("invalid");
            } else {
                login.setVisible(false);
                new MainWindow(br, bw, socket);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Client(String url, int port) {
        try {
            socket = new Socket(url, port);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "端口被占用,程序即将退出");
            System.exit(0);
        }
    }
}