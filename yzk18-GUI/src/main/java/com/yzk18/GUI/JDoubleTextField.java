package com.yzk18.GUI;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JDoubleTextField extends JTextField {
    public JDoubleTextField()
    {
        setDocument(new PlainDocument(){
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null)
                {
                    return;
                }
                char[] s = str.toCharArray();
                int length = 0;
                for (int i = 0; i < s.length; i++)
                {
                    if(isValid(s[i]))
                    {
                        s[length++] = s[i];
                    }
                }
                super.insertString(offset, new String(s, 0, length), attr);
            }
        });
    }
    static boolean isValid(char c)
    {
        return ((c >= '0') && (c <= '9'))||c=='-'||c=='.';
    }

    public void setValue(double d)
    {
        setText(Double.toString(d));
    }

    public Double getValue()
    {
        try
        {
            return Double.parseDouble(getText());
        }
        catch (NumberFormatException ex)
        {
            return null;
        }
    }
}
