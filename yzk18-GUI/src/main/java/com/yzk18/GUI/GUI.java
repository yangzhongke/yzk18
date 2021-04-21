package com.yzk18.GUI;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import com.github.lgooddatepicker.components.*;
import com.github.lgooddatepicker.zinternaltools.JIntegerTextField;
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

    public static String[] multiInputBox(Object message,Collection<String> labels,
                                         Collection<Object> initialValues,Collection<Class> types)
    {
        String[] initValuesArray = null;
        if(initialValues!=null)
        {
            initValuesArray = initialValues.toArray(new String[initialValues.size()]);
        }
        Class[] typesArray = null;
        if(types!=null)
        {
            typesArray = types.toArray(new Class[types.size()]);
        }
        return multiInputBox(message,labels.toArray(new String[labels.size()]),
                initValuesArray,typesArray);
    }

    public static String[] multiInputBox(Object message,String... labels)
    {
        return multiInputBox(message,labels,null,null);
    }

    private static JComponent createEditor(Class type,Object initValue)
    {
        if(type==Integer.class||type==int.class)
        {
            JIntegerTextField txtField = new JIntegerTextField();
            if(initValue!=null)
            {
                txtField.setValue(CommonHelpers.toInt(initValue));
            }
            return txtField;
        }
        else if(type==Boolean.class||type==boolean.class)
        {
            JCheckBox checkBox = new JCheckBox();
            if(initValue!=null)
            {
                checkBox.setSelected(CommonHelpers.toBoolean(initValue));
            }
            return checkBox;
        }
        else if(type==LocalDate.class)
        {
            DatePicker picker = new DatePicker();
            if(initValue!=null)
            {
                picker.setDate(CommonHelpers.toLocalDate(initValue));
            }
            return picker;
        }
        else if(type==LocalTime.class)
        {
            TimePickerSettings timePickerSettings = DateTimePickersUtils.createTimePickerSettings();
            TimePicker picker = new TimePicker(timePickerSettings);
            if(initValue!=null)
            {
                picker.setTime(CommonHelpers.toLocalTime(initValue));
            }
            return picker;
        }
        else if(type==LocalDateTime.class)
        {
            TimePickerSettings timePickerSettings = DateTimePickersUtils.createTimePickerSettings();
            DateTimePicker picker = new DateTimePicker(new DatePickerSettings(),timePickerSettings);
            if(initValue!=null)
            {
                picker.setDateTimeStrict(CommonHelpers.toLocalDateTime(initValue));
            }
            return picker;
        }
        else if(type==File.class)
        {
            FilePicker picker = new FilePicker();
            if(initValue!=null)
            {
                String txt = CommonHelpers.toString(initValue);
                picker.setFile(new File(txt));
            }
            return picker;
        }
        else
        {
            JTextField txtField = new JTextField(CommonHelpers.toString(initValue));
            return txtField;
        }
    }

    private static Object getEditorValue(JComponent componentEditor)
    {
        if(componentEditor instanceof JTextField)
        {
            JTextField txtField = (JTextField)componentEditor;
            return txtField.getText();
        }
        else if(componentEditor instanceof  DatePicker)
        {
            DatePicker picker = (DatePicker)componentEditor;
            return picker.getDate();
        }
        else if(componentEditor instanceof  JCheckBox)
        {
            JCheckBox checkBox = (JCheckBox)componentEditor;
            return checkBox.isSelected();
        }
        else if(componentEditor instanceof  TimePicker)
        {
            TimePicker picker = (TimePicker)componentEditor;
            return picker.getTime();
        }
        else if(componentEditor instanceof  DateTimePicker)
        {
            DateTimePicker picker = (DateTimePicker)componentEditor;
            return picker.getDateTimeStrict();
        }
        else if(componentEditor instanceof  FilePicker)
        {
            FilePicker picker = (FilePicker)componentEditor;
            return picker.getFile();
        }
        else
        {
            throw new IllegalArgumentException("unknow control type:"+componentEditor.getClass());
        }
    }

    public static String[] multiInputBox(Object message,String[] labels,Object[] initialValues,Class[] types)
    {
        if(initialValues!=null&&labels.length<initialValues.length)
        {
            throw new IllegalArgumentException("labels.length<initialValues.length");
        }
        Dimension halfSize = getDefaultDialogSize();

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

        ArrayList<JComponent> editors = new ArrayList<>(fieldsCount);
        for(int i=0;i<fieldsCount;i++)
        {
            String labelText = labels[i];
            Object initValue="";
            if(initialValues!=null&&i<initialValues.length)
            {
                initValue = initialValues[i];
            }
            JLabel label = new JLabel(labelText);
            rootPanel.add(label);

            JComponent componentEditor;
            if(types!=null&&i<types.length)
            {
                Class type = types[i];
                componentEditor = createEditor(type,initValue);
            }
            else
            {
                JTextField txtField = new JTextField(CommonHelpers.toString(initValue));
                componentEditor = txtField;
            }

            if(i==0)
            {
                DefaultFocusAncestorListener.setDefaultFocusedComponent(rootPanel,componentEditor);
            }
            rootPanel.add(componentEditor);
            editors.add(componentEditor);

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
            layout.setConstraints(componentEditor,gbcTxt);
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

        String[] results = new String[editors.size()];
        for(int i=0;i<results.length;i++)
        {
            JComponent componentEditor = editors.get(i);
            Object value = getEditorValue(componentEditor);
            results[i] = CommonHelpers.toString(value);
        }

        return results;
    }

    public static Map<String,String> multiInputBox(Object message,Map<String,Object> data)
    {
        String[] labels = new String[data.size()];
        Object[] initialValues =new Object[data.size()];
        Class[] types = new Class[data.size()];
        int counter=0;
        for(Map.Entry<String,Object> entry : data.entrySet())
        {
            String name = entry.getKey();
            Object value = entry.getValue();
            labels[counter] = name;
            initialValues[counter] = value;
            types[counter] = (value==null?String.class:value.getClass());
            counter++;
        }
        String[] returnedValues = multiInputBox(message,labels,initialValues,types);
        if(returnedValues==null)
        {
            return null;
        }
        Map<String,String> returnedMap = new LinkedHashMap<>();
        for(int i=0;i<labels.length;i++)
        {
            returnedMap.put(labels[i],returnedValues[i]);
        }
        return returnedMap;
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
            String[] labels = new String[props.length];
            Object[] initValues = new Object[props.length];
            Class[] types = new Class[props.length];
            for(int i=0;i<props.length;i++)
            {
                PropertyDescriptor prop = props[i];
                String propName = prop.getName();
                Object propValue = ReflectionHelpers.getPropertyValue(data,prop);
                Class dataType = prop.getPropertyType();
                labels[i] = propName;
                initValues[i] = propValue;
                types[i] = dataType;
            }
            String[] results = multiInputBox(message,labels,initValues,types);
            if(results==null)
            {
                return null;
            }
            Object resultObj = objType.getConstructor().newInstance();
            for(int i=0;i<props.length;i++)
            {
                PropertyDescriptor prop = props[i];
                String propValue = results[i];
                Class dataType = prop.getPropertyType();
                Object convertedValue = CommonHelpers.convert(propValue,dataType);
                ReflectionHelpers.setPropertyValue(resultObj,prop,convertedValue);
            }
            return (T)resultObj;
        } catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException
                |NoSuchMethodException|InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalDate dateBox(Object message)
    {
        return dateBox(message,null);
    }

    public static LocalDate dateBox(Object message, LocalDate initialValue)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(CommonHelpers.toString(message));
        panel.add(label);

        DatePicker datePicker = new DatePicker();
        if(initialValue!=null)
        {
            datePicker.setDate(initialValue);
        }
        panel.add(datePicker);

        int result = JOptionPane.showOptionDialog(null, panel, null,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        return datePicker.getDate();
    }

    public static LocalDateTime datetimeBox(Object message)
    {
        return datetimeBox(message,null);
    }

    public static LocalDateTime datetimeBox(Object message, LocalDateTime initialValue)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(CommonHelpers.toString(message));
        panel.add(label);

        TimePickerSettings timePickerSettings = DateTimePickersUtils.createTimePickerSettings();
        DateTimePicker picker = new DateTimePicker(new DatePickerSettings(),timePickerSettings);
        if(initialValue!=null)
        {
            picker.setDateTimeStrict(initialValue);
        }
        panel.add(picker);

        int result = JOptionPane.showOptionDialog(null, panel, null,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        return picker.getDateTimeStrict();
    }

    public static LocalTime timeBox(Object message)
    {
        return timeBox(message,null);
    }

    public static LocalTime timeBox(Object message, LocalTime initialValue)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(CommonHelpers.toString(message));
        panel.add(label);

        TimePickerSettings timePickerSettings = DateTimePickersUtils.createTimePickerSettings();
        TimePicker picker = new TimePicker(timePickerSettings);
        if(initialValue!=null)
        {
            picker.setTime(initialValue);
        }
        panel.add(picker);

        int result = JOptionPane.showOptionDialog(null, panel, null,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        return picker.getTime();
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
        Dimension halfSize = getDefaultDialogSize();

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
        Dimension halfSize = getDefaultDialogSize();

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

    private static Dimension getDefaultDialogSize() {
        Dimension screenSize = getScreenSize();
        return new Dimension(screenSize.width / 2, screenSize.height / 2);
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

    private static ProgressDialog progressDialog;
    public static void showProgressDialog(Object message,int maximum,int value)
    {
        SwingUtilities.invokeLater(()->{
            if(progressDialog==null)
            {
                progressDialog = new ProgressDialog();
                progressDialog.setModal(false);
                progressDialog.setLocationRelativeTo(null);//screen center
            }
            progressDialog.setMessage(CommonHelpers.toString(message));
            progressDialog.setProgress(maximum, value);
            progressDialog.pack();
            progressDialog.setVisible(true);
        });
    }

    public static void closeProgressDialog()
    {
        progressDialog.setVisible(false);
        progressDialog.dispose();
        progressDialog = null;
    }

    private static IndeterminateProgressDialog indeterminateProgressDialog;
    public static void showIndeterminateProgressDialog(Object message)
    {
        SwingUtilities.invokeLater(()->{
            if(indeterminateProgressDialog==null)
            {
                indeterminateProgressDialog = new IndeterminateProgressDialog();
                indeterminateProgressDialog.setModal(false);
                indeterminateProgressDialog.setLocationRelativeTo(null);//screen center
            }
            indeterminateProgressDialog.setMessage(CommonHelpers.toString(message));
            indeterminateProgressDialog.pack();
            indeterminateProgressDialog.setVisible(true);
        });
    }

    public static void closeIndeterminateProgressDialog()
    {
        indeterminateProgressDialog.setVisible(false);
        indeterminateProgressDialog.dispose();
        indeterminateProgressDialog = null;
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
