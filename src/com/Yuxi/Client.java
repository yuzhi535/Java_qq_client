package com.Yuxi;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    Socket socket;

    ObjectInputStream in;
    ObjectOutputStream out;

    Client(String url, int port) {
        try {
            socket = new Socket(url, port);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "请检查网络是否连接或者点口是否被占用");
        }
    }

    //  有时间把login 和 register 合并
    void register(String user, String passwd, Login login) {
        Thread mythread = null;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("aosjdfasdfi");
            out.writeUTF("register");
            out.flush();

            // validate the username
            out.writeUTF(user);
            out.flush();
            out.writeUTF(passwd);
            out.flush();

            String str = in.readUTF();
            if (str.equals("valid")) {
                new MainWindow(in, out, socket, user, passwd);
                login.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "用户名重复，请重新注册!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void login(String user, String passwd, Login login) {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            out.writeUTF("login");

            out.flush();

            // validate the username
            out.writeUTF(user);
            out.flush();
            out.writeUTF(passwd);
            out.flush();

            String str = in.readUTF();

            if (str.equals("invalid")) {
                JOptionPane.showMessageDialog(null, "没有此用户，请注册或检查填写的信息");
                System.out.println("invalid");
            } else {
                login.setVisible(false);
                new MainWindow(in, out, socket, user, passwd);
            }


        } catch (EOFException e) {
            JOptionPane.showMessageDialog(null, "连接失败");
            System.out.println("invalid");
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }
}