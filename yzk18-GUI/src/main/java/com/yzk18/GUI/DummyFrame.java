package com.yzk18.GUI;


import javax.swing.JFrame;

/**
 * show a dummy frame to enable the item on taskbar.
 * Because a dialog cannot show an item on taskbar.
 */
public class DummyFrame extends JFrame {
    DummyFrame()
    {
        super("");
        setUndecorated(true);
        setVisible(true);
        setLocationRelativeTo(null);
    }
}