package com.yzk18.GUI;

import com.privatejgoodies.forms.factories.CC;
import com.privatejgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class FilePicker extends JPanel {
    private JTextField txtField;
    private JButton btnPicker;

    public FilePicker()
    {
        this.txtField = new JTextField();
        this.btnPicker = new JButton("...");
        setLayout(new FormLayout(
                "pref:grow, [3px,pref], [26px,pref]",
                "fill:pref:grow"));

        txtField.setMargin(new Insets(1, 3, 2, 2));
        txtField.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, new Color(122, 138, 153)),
                new EmptyBorder(1, 3, 2, 2)));

        add(txtField, CC.xy(1, 1));
        add(btnPicker, CC.xy(3, 1));

        btnPicker.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int ret = fileChooser.showOpenDialog(FilePicker.this);
                if(ret!=JFileChooser.APPROVE_OPTION)
                {
                    return;
                }
                File file = fileChooser.getSelectedFile();
                txtField.setText(file.toString());
            }
        });

    }

    public File getFile()
    {
        return new File(this.txtField.getText());
    }

    public void setFile(File file)
    {
        this.txtField.setText(file.toString());
    }
}
