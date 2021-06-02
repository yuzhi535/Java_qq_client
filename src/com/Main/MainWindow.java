package com.Main;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainWindow extends JFrame {

    BufferedReader ir;
    BufferedWriter ow;
    JTextArea area;   // currently use text area. however, i can try using table

    MainWindow(BufferedReader inputStreamReader, BufferedWriter outputStreamWriter) {
        ir = inputStreamReader;
        ow = outputStreamWriter;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
    }
}
