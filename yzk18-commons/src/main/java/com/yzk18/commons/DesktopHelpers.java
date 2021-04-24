package com.yzk18.commons;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * <div lang="zh-cn">桌面操作相关地方法。</div>
 */
public class DesktopHelpers {

    /**
     * <div lang="zh-cn">用默认软件打开文件。</div>
     * @param filename
     */
    public static void openFile(String filename)
    {
        try {
            Desktop.getDesktop().open(new File(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">用默认软件编辑文件。</div>
     * @param filename
     */
    public static void editFile(String filename)
    {
        try {
            Desktop.getDesktop().edit(new File(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn"></div>
     * @param url
     */
    public static void openUrl(String url)
    {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">把value设置到系统剪贴板中。</div>
     * @param value
     */
    public static void setClipboardText(String value)
    {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = new StringSelection(value);
        clipboard.setContents(trans, null);
    }

    /**
     * <div lang="zh-cn">得到系统剪贴板中地字符串类型地数据。</div>
     * @return  <div lang="zh-cn">字符串，如果没有则返回null。</div>
     */
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
