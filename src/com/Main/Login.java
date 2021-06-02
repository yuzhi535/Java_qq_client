package com.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Login extends JFrame {
    JTextField user_name;
    JPasswordField passwd;
    JLabel user_tip;
    JLabel passwd_tip;
    JButton btn;
    JPanel upper_image;
    JPanel loginWindow;
    BufferedImage img;

    Login() {
        super("登陆");
        try {
            img = ImageIO.read(new File("resources/qqLogin.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        setLayout(new BorderLayout());
        upper_image = new JPanel();
        upper_image.add(new JLabel(new ImageIcon(img)));
        loginWindow = new JPanel();

        loginWindow.setLayout(new FlowLayout());
        user_name = new JTextField("your name", 6);
        passwd = new JPasswordField( "your password", 6);
        user_tip = new JLabel("用户名");
        passwd_tip = new JLabel("密码");
        btn = new JButton("确定");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = user_name.getText();
                String pawd = String.valueOf(passwd.getPassword());
                Client client = new Client("49.232.155.89", 6666, name, pawd);
                setVisible(false);
            }
        });
//        upper_image.setBounds(0, 0, 400, 400);
//        loginWindow.setBounds(0, 400, 400, 200);

        loginWindow.add(user_tip);
        loginWindow.add(user_name);
        loginWindow.add(passwd_tip);
        loginWindow.add(passwd);
        loginWindow.add(btn);
        loginWindow.setBackground(Color.WHITE);


        add(upper_image, BorderLayout.NORTH);
        add(loginWindow, BorderLayout.CENTER);
        setSize(300, 500);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
