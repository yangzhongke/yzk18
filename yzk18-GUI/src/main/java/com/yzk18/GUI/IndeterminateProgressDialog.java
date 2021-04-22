package com.yzk18.GUI;

import com.yzk18.commons.CommonHelpers;

import javax.swing.*;
import java.awt.*;

public class IndeterminateProgressDialog extends JDialog {
    private JLabel label;
    public IndeterminateProgressDialog(Frame owner)
    {
        super(owner);
        initComponents();
    }

    public IndeterminateProgressDialog(Dialog owner)
    {
        super(owner);
        initComponents();
    }

    public IndeterminateProgressDialog()
    {
        super();
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.label = new JLabel("");
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(label);
        panel.add(progressBar);
        panel.add(new Label());
        setContentPane(panel);

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setResizable(false);//hide icon
    }

    public void setMessage(String message)
    {
        this.label.setText(message);
    }
}
