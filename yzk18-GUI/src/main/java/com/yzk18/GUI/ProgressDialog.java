package com.yzk18.GUI;

import javax.swing.*;
import java.awt.*;

public class ProgressDialog extends JDialog {
    private JLabel label;
    private JProgressBar progressBar;
    public ProgressDialog(Dialog owner)
    {
        super(owner);
        initComponents();
    }

    public ProgressDialog(Frame owner)
    {
        super(owner);
        initComponents();
    }

    public ProgressDialog()
    {
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.label = new JLabel("");
        this.progressBar = new JProgressBar();
        panel.add(label);
        panel.add(progressBar);
        panel.add(new Label());
        setContentPane(panel);

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setResizable(false);//hide icon
    }

    public void setProgress(int maximum,int value)
    {
        progressBar.setMaximum(maximum);
        progressBar.setValue(value);
    }

    public void setMessage(String message)
    {
        this.label.setText(message);
    }
}