package com.Yuxi;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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
    Choice ip;
    JButton confirmNetworkBtn;
    String use_ip;
    JLabel ip_tip;
    int index = 0;
    HashMap<String, String> ip_map;

    Login() {
        super("登陆");
        ip_map = new HashMap<>();
        use_ip = "49.232.155.89";
        ip_map.put("云端", use_ip);
        ip_map.put("本地", "localhost");


        try {
            img = ImageIO.read(new File("resources/qqLogin.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        card = new CardLayout();

        upper_image = new JPanel();   // logo图片容器
        upper_image.add(new JLabel(new ImageIcon(img)));
        loginWindow = new JPanel();  //登陆
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
        ip_tip = new JLabel("IP地址");

        ip = new Choice();
        ip.add("云端服务器");
        ip.add("本地服务器");

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

            String[] tips = new String[]{"设置网络", "取消"};


            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                card.next(chooseNet);
                setNetBtn.setText(tips[(++index) % 2]);
            }
        });
        confirmNetworkBtn.addActionListener(new ActionListener() {
            String[] tips = new String[]{"设置网络", "取消"};

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                use_ip = ip_map.get(ip.getSelectedItem());
                card.next(chooseNet);
                setNetBtn.setText(tips[(++index) % 2]);
            }
        });

        // 加入这两个容器卡片选择
        chooseNet.add(loginWindow);
        chooseNet.add(choice);
        choice.add(ip);
        choice.add(confirmNetworkBtn);
        choice.add(ip_tip);
        choice.setBackground(Color.WHITE);
        choice.setLayout(null);
        ip.setBounds(110, 50, 100, 30);
        confirmNetworkBtn.setBounds(100, 100, 100, 30);
        ip_tip.setBounds(65, 50, 45, 30);

        loginWindow.setLayout(null);

        loginWindow.add(user_tip);
        loginWindow.add(user_name);
        loginWindow.add(passwd_tip);
        loginWindow.add(passwd);
        loginWindow.add(confirmBtn);
        loginWindow.add(registerBtn);

        user_tip.setBounds(50, 45, 50, 25);
        user_name.setBounds(105, 45, 140, 25);
        passwd_tip.setBounds(50, 80, 50, 25);
        passwd.setBounds(105, 80, 140, 25);
        confirmBtn.setBounds(40, 150, 100, 25);
        registerBtn.setBounds(150, 150, 100, 25);
        loginWindow.setBackground(Color.WHITE);


        add(upper_image);
        add(switchCard);
        add(chooseNet);

        setLayout(null);
        upper_image.setBounds(0, 0, 300, 200);
        chooseNet.setBounds(0, 200, 300, 200);
        switchCard.setBounds(0, 400, 300, 100);
        chooseNet.setBackground(Color.WHITE);
        switchCard.setBackground(Color.WHITE);

        setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 300,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 500, 300, 500);

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


