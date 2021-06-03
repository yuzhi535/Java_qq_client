package com.Yuxi;

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
    JButton confirmBtn;
    JButton registerBtn;
    JPanel upper_image;
    JPanel loginWindow;  // 登陆容器
    JPanel chooseNet;   // 选择网络的容器
    JPanel network;    // 设置IP
    JPanel switchCard;
    JPanel choice;     // 卡片
    BufferedImage img;
    JButton setNetBtn;
    CardLayout card;
    JTextField ip;
    JButton confirmNetworkBtn;
    String use_ip;
    FlowLayout loginLayout;

    Login() {
        super("登陆");

        use_ip = "49.232.155.89";
        try {
            img = ImageIO.read(new File("resources/qqLogin.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        card = new CardLayout();
        setLayout(new BorderLayout());
        loginLayout = new FlowLayout();
        loginLayout.setVgap(2);

        upper_image = new JPanel();   // logo图片容器
        upper_image.add(new JLabel(new ImageIcon(img)));
        loginWindow = new JPanel();  //登陆
        loginWindow.setLayout(loginLayout);
        chooseNet = new JPanel();    // 选择登陆容器还是网络配置
        chooseNet.setLayout(card);
        network = new JPanel();
        network.setLayout(new FlowLayout());
        choice = new JPanel();
        choice.setLayout(new FlowLayout());
        setNetBtn = new JButton("设置网络");
        switchCard = new JPanel();
        switchCard.add(setNetBtn);
        confirmNetworkBtn = new JButton("确定");
        ip = new JTextField("", 6);

        // 加入这两个容器卡片选择
        chooseNet.add(loginWindow);
        chooseNet.add(choice);
        choice.add(ip);
        choice.add(confirmNetworkBtn);


        user_name = new JTextField("", 14);
        passwd = new JPasswordField("", 14);
        user_tip = new JLabel("用户名");
        passwd_tip = new JLabel("密码");
        confirmBtn = new JButton("登陆");
        registerBtn = new JButton("注册");

        BtnActionListener btnActionListener = new BtnActionListener(this);

        confirmBtn.addActionListener(btnActionListener);
        registerBtn.addActionListener(btnActionListener);
        setNetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                card.next(chooseNet);
            }
        });
        confirmNetworkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                use_ip = ip.getText();
                card.next(chooseNet);
            }
        });
//        upper_image.setBounds(0, 0, 400, 400);
//        loginWindow.setBounds(0, 400, 400, 200);

        loginWindow.add(user_tip);
        loginWindow.add(user_name);
        loginWindow.add(passwd_tip);
        loginWindow.add(passwd);
        loginWindow.add(confirmBtn);
        loginWindow.add(registerBtn);
        loginWindow.setBackground(Color.WHITE);


        add(upper_image, BorderLayout.NORTH);
        add(switchCard, BorderLayout.SOUTH);
        add(chooseNet, BorderLayout.CENTER);
        setSize(300, 500);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    class BtnActionListener implements ActionListener {
        Login me;

        BtnActionListener(Login aim) {
            me = aim;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String name = user_name.getText();
            String pawd = String.valueOf(passwd.getPassword());

            if (name.equals("") || pawd.equals("")) {
                JOptionPane.showMessageDialog(null, "invalid username or passwd!");
                return;
            }
//            Client client = new Client("127.0.0.1", 6666);
            Client client = new Client(use_ip, 6666);
            if (actionEvent.getActionCommand().equals("登陆")) {
                client.login(name, pawd, me);
            } else {
                client.register(name, pawd, me);
            }
        }
    }
}


