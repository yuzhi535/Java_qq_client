package com.Yuxi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    //    private ImageFrame loadImageFrame;
    private JButton selectImageBtn;
    private JButton sendFileBtn;
    private String path;
    private JLabel pathLabel;

    // 成员列表
    private JTable usersTable;
    private final String[] user_groups = new String[]{"成员"};
    private JScrollPane jScrollPane3;

    private DefaultTableModel tableModel;    //获得表格模型

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
//        loadImageFrame = new ImageFrame();
        selectImageBtn = new JButton("选择文件");
        sendFileBtn = new JButton("发送文件");
        pathLabel = new JLabel("文件路径");

        String[][] temp = new String[1][1];
        temp[0][0] = "";
        tableModel = new DefaultTableModel(temp, user_groups);
        usersTable = new JTable(tableModel);
        jScrollPane3 = new JScrollPane(usersTable);
//        usersTable.set

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

        selectImageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                int re = fileChooser.showDialog(null, "选择");
                if (re == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    path = file.getAbsolutePath();
                    pathLabel.setText(path);
                }
            }
        });

        sendFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File(path);
                byte[] data = null;
                double loop = 0;
                int length;
                try {
                    FileInputStream fs = new FileInputStream(file);
                    length = fs.readAllBytes().length;
                    fs.skip(-length);

                    System.out.println("length = " + length);
                    if (length != 0) {
                        int index = 1;
                        loop = length / 1024.0;
                        data = new byte[1024];
                        for (; index - 1 <= loop; ++index) {
                            int offset = (index - 1) * 1024;
                            int re = fs.read(data, 0, 1024);
                            if (re != -1) {
                                System.out.println(Arrays.toString(data) + "-----------\n");
                                User info = new User(user_name, passwd, index, 2,
                                        offset, file.getName(), data, length);
                                out.writeObject(info);
                                out.flush();
                            } else {
                                JOptionPane.showMessageDialog(null, "文件读取出错！");
                                break;
                            }
                        }
                    }
                    fs.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }

            }
        });


        setLayout(new FlowLayout());

        add(jScrollPane2);
        add(jScrollPane1);
        add(sendBtn);
        add(clsBtn);
//        add(loadImageFrame);
        add(jScrollPane3);
        add(selectImageBtn);
        add(sendFileBtn);
        add(pathLabel);

        setLayout(null);

        jScrollPane1.setBounds(0, 0, 380, 150);
        jScrollPane2.setBounds(0, 160, 380, 150);
        jScrollPane3.setBounds(400, 0, 150, 250);
        sendBtn.setBounds(80, 320, 100, 30);
        clsBtn.setBounds(200, 320, 100, 30);

//        loadImageFrame.setBounds(330, 0, 200, 350);
        pathLabel.setBounds(400, 260, 80, 40);
        selectImageBtn.setBounds(400, 320, 80, 30);
        sendFileBtn.setBounds(500, 320, 80, 30);

        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 600,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 400, 600, 400);

        setVisible(true);
        new Handle().start();    // read the remote msg

        // 时刻发送成员列表
        Thread daemonThread = new Thread(() -> {
            while (true) {
                String str = "group";
                User info = new User(user_name, passwd, 1, 3, str.length(), "asd",
                        str.getBytes(StandardCharsets.UTF_8), str.length());
                try {
                    out.writeObject(info);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);   // 每一秒发送请求
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        daemonThread.setDaemon(true);    // 设置守护线程
        daemonThread.start();
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
        String user = "";

        @Override
        public void run() {
            byte[] rawDat = null;
            while (true) {
                try {
                    info = (User) in.readObject();
                    index = info.getIndex();
                    data = info.getData();
                    totalSize = info.getTotal_size();
                    type = info.getType();
                    dataSize = info.getData_size();
                    user = info.getUser_name();
//                    System.out.println("datasize = " + dataSize + " and totalsize = " + totalSize);
                    if (index == 1)
                        rawDat = new byte[totalSize];
                    if (totalSize - dataSize < 1024) {
                        if (type == 1) {
//                            System.out.println(Arrays.toString(data));
                            infoString += new String(data);
                            infoString = user + ":  " + infoString;
                            getTText(infoString);
                            infoString = "";
                            data = null;
                        } else if (type == 2) {
                            System.arraycopy(data, 0, rawDat, (index - 1) * 1024, totalSize - dataSize);
                            users = info.getGroup();
                            File file = new File(users);
                            FileOutputStream fs = new FileOutputStream(file);
                            fs.write(rawDat);
                            fs.flush();
                            fs.close();
                        } else {   // 请求成员列表
                            users += info.getGroup();
                            String[] userList;
                            int table_index = 1;
                            String delimeter = "\r";  // 指定分割字符
                            userList = users.split(delimeter);
                            for (int i = tableModel.getRowCount() - 1; i >= 0; --i) {
                                tableModel.removeRow(i);
                            }
                            for (int i = 1; i < userList.length; ++i) {
//                                System.out.println(userList[i]);
                                tableModel.addRow(new String[]{userList[i]});
                            }
                        }
                        dataSize = 0;
                        users = "";
                        info = null;
                        users = "";
                        rawDat = null;
                    } else {
                        if (type == 1) {
//                            System.out.println(Arrays.toString(data));
                            infoString += new String(data);
                        } else if (type == 2) {
                            System.arraycopy(data, 0, rawDat, dataSize, 1024);
                        } else {
                            users += info.getGroup();
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

//    @Override
//    public void paintComponents(Graphics g) {
//        super.paintComponents(g);
//        g.draw
//    }
}
