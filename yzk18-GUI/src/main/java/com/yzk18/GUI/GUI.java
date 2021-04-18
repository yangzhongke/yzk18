package com.yzk18.GUI;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.ReflectionHelpers;


public class GUI {

    static
    {
        if(Locale.SIMPLIFIED_CHINESE.equals(Locale.getDefault()))
        {
            //https://stackoverflow.com/questions/14407804/how-to-change-the-default-text-of-buttons-in-joptionpane-showinputdialog/14408288
            UIManager.put("OptionPane.userNameText", "用户名");
            UIManager.put("OptionPane.passwordText", "密码");
            UIManager.put("JFileChoose.saveExistConfirmText","选中内容已经存在，确认覆盖吗？");
        }
        else
        {
            UIManager.put("OptionPane.userNameText", "UserName");
            UIManager.put("OptionPane.passwordText", "Password");
            UIManager.put("JFileChoose.saveExistConfirmText","This item exists, overwrite?");
        }
    }
    public static void msgBox(Object message)
    {
        JOptionPane.showMessageDialog(null,CommonHelpers.toString(message),null,JOptionPane.INFORMATION_MESSAGE);
    }


    public static void errorBox(Object message)
    {
        JOptionPane.showMessageDialog(null,CommonHelpers.toString(message),null,JOptionPane.ERROR_MESSAGE);
    }

    public static boolean yesNoBox(Object message)
    {
        int ret = JOptionPane.showConfirmDialog(null,CommonHelpers.toString(message),null,JOptionPane.YES_NO_OPTION);
        return ret==JOptionPane.YES_OPTION;
    }

    public static boolean okCancelBox(Object message)
    {
        int ret = JOptionPane.showConfirmDialog(null,CommonHelpers.toString(message),null,JOptionPane.OK_CANCEL_OPTION);
        return ret==JOptionPane.OK_OPTION;
    }

    public static String inputBox(Object message,Object initialValue)
    {
        Object ret = JOptionPane.showInputDialog(null,CommonHelpers.toString(message),null,JOptionPane.PLAIN_MESSAGE,
                null,null,initialValue);
        if(ret==null)
        {
            return "";
        }
        else
        {
            return ret.toString();
        }
    }

    public static String inputBox(Object message)
    {
        return inputBox(message,"");
    }

    public static int intBox(Object message,Integer initialValue)
    {
        Object ret = JOptionPane.showInputDialog(null,CommonHelpers.toString(message),null,JOptionPane.PLAIN_MESSAGE,
                null,null,initialValue);
        if(ret==null)
        {
            return 0;
        }
        else
        {
            try
            {
                String s = ret.toString();
                return Integer.parseInt(s);
            }
            catch (NumberFormatException ex)
            {
                return 0;
            }
        }
    }

    public static int intBox(Object message)
    {
        return intBox(message,null);
    }

    public static String choiceBox(Object message, String initialValue, Collection<String> selectionItems)
    {
        return choiceBox(message,initialValue,selectionItems.toArray(new String[selectionItems.size()]));
    }

    public static String choiceBox(Object message, String initialValue, String... selectionItems)
    {
        Object ret = JOptionPane.showInputDialog(null,CommonHelpers.toString(message),null,JOptionPane.PLAIN_MESSAGE,
                null,selectionItems,initialValue);
        return (String)ret;
    }

    public static String choiceBox(Object message, Collection<String> selectionItems)
    {
        return choiceBox(message,selectionItems.toArray(new String[selectionItems.size()]));
    }

    public static String choiceBox(Object message, String... selectionItems)
    {
        return choiceBox(message,"",selectionItems);
    }

    public static String[] multiInputBox(Object message,Collection<String> labels,Collection<Object> initialValues)
    {
        String[] initValuesArray = null;
        if(initialValues!=null)
        {
            initValuesArray = initialValues.toArray(new String[initialValues.size()]);
        }
        return multiInputBox(message,labels.toArray(new String[labels.size()]),initValuesArray);
    }

    public static String[] multiInputBox(Object message,String... labels)
    {
        return multiInputBox(message,labels,null);
    }
    public static String[] multiInputBox(Object message,String[] labels,Object[] initialValues)
    {
        if(initialValues!=null&&labels.length<initialValues.length)
        {
            throw new IllegalArgumentException("labels.length<initialValues.length");
        }
        Dimension screenSize = getScreenSize();
        Dimension halfSize = new Dimension(screenSize.width/2,screenSize.height/2);

        int fieldsCount = labels.length;

        JPanel rootPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        rootPanel.setLayout(layout);

        JLabel labelMsg = new JLabel(CommonHelpers.toString(message));
        rootPanel.add(labelMsg);

        GridBagConstraints gbcLabelMsg = new GridBagConstraints();
        gbcLabelMsg.gridx=0;
        gbcLabelMsg.gridy=0;
        gbcLabelMsg.gridwidth=2;
        layout.setConstraints(labelMsg,gbcLabelMsg);

        ArrayList<JTextField> txtFields = new ArrayList<>(fieldsCount);
        for(int i=0;i<fieldsCount;i++)
        {
            String labelText = labels[i];
            String initValue="";
            if(initialValues!=null&&i<initialValues.length)
            {
                initValue = CommonHelpers.toString(initialValues[i]);
            }
            JLabel label = new JLabel(labelText);
            rootPanel.add(label);
            JTextField txtField = new JTextField(initValue);
            rootPanel.add(txtField);

            if(i==0)
            {
                DefaultFocusAncestorListener.setDefaultFocusedComponent(rootPanel,txtField);
            }

            txtFields.add(txtField);

            GridBagConstraints gbcLabel = new GridBagConstraints();
            gbcLabel.gridx=0;
            gbcLabel.gridy=i+1;
            gbcLabel.gridwidth=1;
            layout.setConstraints(label,gbcLabel);

            GridBagConstraints gbcTxt = new GridBagConstraints();
            gbcTxt.gridx=1;
            gbcTxt.gridy=i+1;
            gbcTxt.weightx=1;
            gbcTxt.gridwidth=1;
            gbcTxt.fill = GridBagConstraints.HORIZONTAL;
            layout.setConstraints(txtField,gbcTxt);
        }

        JScrollPane scrollPane = new JScrollPane(rootPanel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(halfSize);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        int result = JOptionPane.showConfirmDialog(null, scrollPane,
                null, JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }

        return txtFields.stream().map(tf->tf.getText()).toArray(String[]::new);
    }

    public static Map<String,String> multiInputBox(Object message,Map<String,Object> data)
    {
        Dimension screenSize = getScreenSize();
        Dimension halfSize = new Dimension(screenSize.width/2,screenSize.height/2);

        JPanel rootPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        rootPanel.setLayout(layout);

        JLabel labelMsg = new JLabel(CommonHelpers.toString(message));
        rootPanel.add(labelMsg);

        GridBagConstraints gbcLabelMsg = new GridBagConstraints();
        gbcLabelMsg.gridx=0;
        gbcLabelMsg.gridy=0;
        gbcLabelMsg.gridwidth=2;
        layout.setConstraints(labelMsg,gbcLabelMsg);

        int counter=0;
        HashMap<String,JTextField> txtFields = new HashMap<String,JTextField>();
        for(Map.Entry<String,Object> entry : data.entrySet())
        {
            String labelText = entry.getKey();
            String initValue = CommonHelpers.toString(entry.getValue());
            JLabel label = new JLabel(labelText);
            rootPanel.add(label);
            JTextField txtField = new JTextField(initValue);
            rootPanel.add(txtField);

            if(counter==0)
            {
                DefaultFocusAncestorListener.setDefaultFocusedComponent(rootPanel,txtField);
            }
            txtFields.put(labelText,txtField);

            GridBagConstraints gbcLabel = new GridBagConstraints();
            gbcLabel.gridx=0;
            gbcLabel.gridy=counter+1;
            gbcLabel.gridwidth=1;
            layout.setConstraints(label,gbcLabel);

            GridBagConstraints gbcTxt = new GridBagConstraints();
            gbcTxt.gridx=1;
            gbcTxt.gridy=counter+1;
            gbcTxt.weightx=1;
            gbcTxt.gridwidth=1;
            gbcTxt.fill = GridBagConstraints.HORIZONTAL;
            layout.setConstraints(txtField,gbcTxt);
            counter++;
        }

        JScrollPane scrollPane = new JScrollPane(rootPanel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(halfSize);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        int result = JOptionPane.showConfirmDialog(null, scrollPane,
                null, JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        Map<String,String> results = new HashMap<String,String>();
        for(Map.Entry<String,Object> entry : data.entrySet())
        {
            String labelText = entry.getKey();
            JTextField txtField = txtFields.get(labelText);
            results.put(labelText,txtField.getText());
        }

        return results;
    }



    public static <T> T objectInputBox(Object message,T data)
    {
        if(data==null)
        {
            throw new IllegalArgumentException("data==null");
        }
        //BeanUtils of commons-lang3 not works for chain setter,
        //so use Introspector instead
        //https://stackoverflow.com/questions/22743765/beanutils-not-works-for-chain-setter
        Class objType = data.getClass();
        try {
            PropertyDescriptor[] props = ReflectionHelpers.getRWPropertyDescriptors(objType);
            Map<String,Object> inputValues = new LinkedHashMap<>();
            for(PropertyDescriptor prop : props)
            {
                String propName = prop.getName();
                Object propValue = ReflectionHelpers.getPropertyValue(data,prop);
                inputValues.put(propName,propValue);
            }
            Map<String,String> results = multiInputBox(message,inputValues);
            if(results==null)
            {
                return null;
            }
            Object resultObj = objType.getConstructor().newInstance();
            for(Map.Entry<String,String> entry : results.entrySet())
            {
                String name = entry.getKey();
                String value = entry.getValue();
                PropertyDescriptor propDesc = ReflectionHelpers.getPropertyDescriptor(data,name);
                Class propType = propDesc.getPropertyType();
                Object convertedValue = CommonHelpers.convert(value,propType);
                ReflectionHelpers.setPropertyValue(resultObj,propDesc,convertedValue);
            }
            return (T)resultObj;
        } catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException
                |NoSuchMethodException|InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String passwordBox(Object message)
    {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(CommonHelpers.toString(message));
        JPasswordField pwdField = new JPasswordField(10);
        panel.add(label);
        panel.add(pwdField);

        DefaultFocusAncestorListener.setDefaultFocusedComponent(panel,pwdField);

        int result = JOptionPane.showOptionDialog(null, panel, null,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        String pwd = new String(pwdField.getPassword());
        return pwd;
    }

    public static String[] loginBox(Object message)
    {
        JPanel rootPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        rootPanel.setLayout(layout);

        JLabel labelMsg = new JLabel(CommonHelpers.toString(message));
        rootPanel.add(labelMsg);

        GridBagConstraints gbcLabelMsg = new GridBagConstraints();
        gbcLabelMsg.gridx=0;
        gbcLabelMsg.gridy=0;
        gbcLabelMsg.gridwidth=2;
        layout.setConstraints(labelMsg,gbcLabelMsg);

        JLabel labelUserName = new JLabel(UIManager.getString("OptionPane.userNameText"));
        rootPanel.add(labelUserName);
        JTextField tfUserName = new JTextField("");
        rootPanel.add(tfUserName);
        GridBagConstraints gbcUserNameLabel = new GridBagConstraints();
        gbcUserNameLabel.gridx=0;
        gbcUserNameLabel.gridy=1;
        gbcUserNameLabel.gridwidth=1;
        layout.setConstraints(labelUserName,gbcUserNameLabel);

        GridBagConstraints gbcUserNameTxt = new GridBagConstraints();
        gbcUserNameTxt.gridx=1;
        gbcUserNameTxt.gridy=1;
        gbcUserNameTxt.weightx=1;
        gbcUserNameTxt.gridwidth=1;
        gbcUserNameTxt.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(tfUserName,gbcUserNameTxt);

        JLabel labelPassword = new JLabel(UIManager.getString("OptionPane.passwordText"));
        rootPanel.add(labelPassword);
        JPasswordField pfPassword = new JPasswordField();
        rootPanel.add(pfPassword);

        GridBagConstraints gbcPwdLabel = new GridBagConstraints();
        gbcPwdLabel.gridx=0;
        gbcPwdLabel.gridy=2;
        gbcPwdLabel.gridwidth=1;
        layout.setConstraints(labelPassword,gbcPwdLabel);

        GridBagConstraints gbcPwdField = new GridBagConstraints();
        gbcPwdField.gridx=1;
        gbcPwdField.gridy=2;
        gbcPwdField.weightx=1;
        gbcPwdField.gridwidth=1;
        gbcPwdField.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(pfPassword,gbcPwdField);

        DefaultFocusAncestorListener.setDefaultFocusedComponent(rootPanel,tfUserName);

        int result = JOptionPane.showConfirmDialog(null, rootPanel,
                null, JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        String userName = tfUserName.getText();
        String password = new String(pfPassword.getPassword());
        return new String[]{userName,password};
    }

    public  static String buttonsBox(Object message,Collection<String> buttons)
    {
        return buttonsBox(message,buttons.toArray(new String[buttons.size()]));
    }

    public  static String buttonsBox(Object message,String... buttons)
    {
        int result = JOptionPane.showOptionDialog(null, CommonHelpers.toString(message), null,
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, buttons, null);
        if(result<0)
        {
            return null;
        }
        else
        {
            return buttons[result];
        }
    }

    public static String imgBox(Object message,String imgPath,String... buttons)
    {
        Dimension screenSize = getScreenSize();
        Dimension halfSize = new Dimension(screenSize.width/2,screenSize.height/2);

        ImageIcon imgIcon = new ImageIcon(imgPath);

        JPanel panel = new JPanel();
        panel.setPreferredSize(halfSize);
        JLabel labelMsg = new JLabel(CommonHelpers.toString(message));
        panel.add(labelMsg);

        JLabel labelImg = new JLabel(imgIcon);
        JScrollPane scrollPane = new JScrollPane(labelImg);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane);

        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        int result = JOptionPane.showOptionDialog(null, panel, null,
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, buttons, null);
        if(result<0)
        {
            return null;
        }
        else
        {
            return buttons[result];
        }
    }

    public static String textBox(Object message,String text,Collection<String> buttons)
    {
        String[] buttonsArray = null;
        if(buttons!=null)
        {
            buttonsArray = buttons.toArray(new String[buttons.size()]);
        }
        return textBox(message,text,buttonsArray);
    }
    public static String textBox(Object message,String text,String... buttons)
    {
        Dimension screenSize = getScreenSize();
        Dimension halfSize = new Dimension(screenSize.width/2,screenSize.height/2);

        JPanel panel = new JPanel();
        panel.setPreferredSize(halfSize);
        JLabel label = new JLabel(CommonHelpers.toString(message));
        JTextArea txtArea = new JTextArea(text);
        txtArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(label);
        panel.add(scrollPane);
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        DefaultFocusAncestorListener.setDefaultFocusedComponent(panel,txtArea);

        int result = JOptionPane.showOptionDialog(null, panel, null,
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, buttons, null);
        if(result<0)
        {
            return null;
        }
        else
        {
            return buttons[result];
        }
    }

    private static  void setFileFilter(JFileChooser fileChooser,String... extensions)
    {
        if(!CommonHelpers.isEmpty(extensions))
        {
            String desc = String.join("|",
                    Arrays.stream(extensions).map(f->"*."+f).toArray(String[]::new));
            fileChooser.setFileFilter(new FileNameExtensionFilter(desc,extensions));
        }
    }

    public static String fileOpenBox(String title,String... extensions)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        setFileFilter(fileChooser,extensions);
        int ret = fileChooser.showOpenDialog(null);
        if(ret!=JFileChooser.APPROVE_OPTION)
        {
            return null;
        }
        return fileChooser.getSelectedFile().toString();
    }

    public static String[] filesOpenBox(String title,String... extensions)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setMultiSelectionEnabled(true);
        setFileFilter(fileChooser,extensions);
        int ret = fileChooser.showOpenDialog(null);
        if(ret!=JFileChooser.APPROVE_OPTION)
        {
            return null;
        }
        return Arrays.stream(fileChooser.getSelectedFiles()).map(f->f.toString()).toArray(String[]::new);
    }

    private static JFileChooser createSaveJFileChooser()
    {
        JFileChooser fileChooser = new JFileChooser(){
            @Override
            public void approveSelection() {
                String confirmText = UIManager.getString("JFileChoose.saveExistConfirmText");
                File f = getSelectedFile();
                if (f.exists()) {
                    int result = JOptionPane.showConfirmDialog(this,
                            confirmText, null,
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        case JOptionPane.CANCEL_OPTION:
                            cancelSelection();
                            return;
                        default:
                            return;
                    }
                }
                super.approveSelection();
            }
        };
        return fileChooser;
    }

    public static String fileSaveBox(String title,String... extensions)
    {
        JFileChooser fileChooser = createSaveJFileChooser();
        fileChooser.setDialogTitle(title);
        setFileFilter(fileChooser,extensions);
        int ret = fileChooser.showSaveDialog(null);
        if(ret!=JFileChooser.APPROVE_OPTION)
        {
            return null;
        }
        return fileChooser.getSelectedFile().toString();
    }

    public static String dirOpenBox(String title)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret = fileChooser.showOpenDialog(null);
        if(ret!=JFileChooser.APPROVE_OPTION)
        {
            return null;
        }
        return fileChooser.getSelectedFile().toString();
    }

    public static String dirSaveBox(String title)
    {
        JFileChooser fileChooser = createSaveJFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret = fileChooser.showSaveDialog(null);
        if(ret!=JFileChooser.APPROVE_OPTION)
        {
            return null;
        }
        return fileChooser.getSelectedFile().toString();
    }

    public static Dimension getScreenSize()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize;
    }

    public static int getScreenWidth()
    {
        Dimension screenSize = getScreenSize();
        return screenSize.width;
    }

    public static int getScreenHeight()
    {
        Dimension screenSize = getScreenSize();
        return screenSize.height;
    }
}
