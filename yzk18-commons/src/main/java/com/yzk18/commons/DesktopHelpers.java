package com.yzk18.commons;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DesktopHelpers {
    public static void openFile(String filename)
    {
        try {
            Desktop.getDesktop().open(new File(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editFile(String filename)
    {
        try {
            Desktop.getDesktop().edit(new File(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void openUrl(String url)
    {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setClipboardText(String value)
    {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = new StringSelection(value);
        clipboard.setContents(trans, null);
    }

    public static String getClipboardText()
    {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = clipboard.getContents(null);
        if(trans==null)
        {
            return null;
        }
        if (!trans.isDataFlavorSupported(DataFlavor.stringFlavor))
        {
            return null;
        }
        try
        {
            String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
            return text;
        } catch (IOException | UnsupportedFlavorException e)
        {
            throw new RuntimeException(e);
        }
    }
}
