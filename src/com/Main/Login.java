package com.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    JTextField user_name;
    JPasswordField passwd;
    JLabel ip_tip;
    JLabel port_tip;
    JButton btn;

    Login() {
        user_name = new JTextField(null, "127.0.0.1", 30);
        passwd = new JPasswordField(null, "6666", 30);
        ip_tip = new JLabel("用户名");
        port_tip = new JLabel("密码");
        btn = new JButton("确定");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = user_name.getText();
                String pawd = passwd.getText();
                Client client = new Client("127.0.0.1", 7777);
                setVisible(false);
            }
        });
        add(ip_tip);
        add(user_name);
        add(port_tip);
        add(passwd);
        add(btn);
        setLayout(new FlowLayout());
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(320, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
